/*
 *
 * TurbineRotorComponentEntity.java
 *
 * This file is part of Extreme Reactors 2 by ZeroNoRyouki, a Minecraft mod.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *
 * DO NOT REMOVE OR EDIT THIS HEADER
 *
 */

package it.zerono.mods.extremereactors.gamecontent.multiblock.turbine.part;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.zerono.mods.extremereactors.gamecontent.Content;
import it.zerono.mods.extremereactors.gamecontent.multiblock.common.part.AbstractMultiblockEntity;
import it.zerono.mods.extremereactors.gamecontent.multiblock.turbine.MultiblockTurbine;
import it.zerono.mods.extremereactors.gamecontent.multiblock.turbine.TurbinePartType;
import it.zerono.mods.extremereactors.gamecontent.multiblock.turbine.rotor.RotorBladeState;
import it.zerono.mods.extremereactors.gamecontent.multiblock.turbine.rotor.RotorShaftState;
import it.zerono.mods.extremereactors.gamecontent.multiblock.turbine.variant.IMultiblockTurbineVariant;
import it.zerono.mods.extremereactors.gamecontent.multiblock.turbine.variant.TurbineVariant;
import it.zerono.mods.zerocore.lib.CodeHelper;
import it.zerono.mods.zerocore.lib.block.INeighborChangeListener;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import it.zerono.mods.zerocore.lib.world.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TurbineRotorComponentEntity
        extends AbstractTurbineEntity
        implements INeighborChangeListener {

    public static TurbineRotorComponentEntity shaft() {
        return new TurbineRotorComponentEntity(TurbinePartType.RotorShaft, Content.TileEntityTypes.TURBINE_ROTORSHAFT.get());
    }

    public static TurbineRotorComponentEntity blade() {
        return new TurbineRotorComponentEntity(TurbinePartType.RotorBlade, Content.TileEntityTypes.TURBINE_ROTORBLADE.get());
    }

    public static RotorShaftState computeShaftState(final TurbineRotorComponentEntity shaft) {
        return computeShaftState(shaft, true);
    }

    public static RotorBladeState computeBladeState(final TurbineRotorComponentEntity blade) {
        return computeBladeState(blade, true);
    }

    public boolean isShaft() {
        return TurbinePartType.RotorShaft == this._componentType;
    }

    public boolean isBlade() {
        return TurbinePartType.RotorBlade == this._componentType;
    }

    //region INeighborChangeListener

    /**
     * Called when a neighboring Block on a side of this TileEntity changes
     *
     * @param state            the BlockState of this TileEntity block
     * @param neighborPosition position of neighbor
     */
    @Override
    public void onNeighborBlockChanged(final BlockState state, final BlockPos neighborPosition, final boolean isMoving) {

        if (this.getPartWorldOrFail().getBlockState(neighborPosition).getBlock() instanceof TurbineRotorComponentBlock) {
            this.requestClientRenderUpdate();
        }
    }

    //endregion
    //region AbstractReactorEntity

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {

        if (PartPosition.Interior != position) {

            validatorCallback.setLastError("multiblock.validation.turbine.invalid_rotor_position", this.getWorldPosition());
            return false;
        }

        return true;
    }

    /**
     * Called when the user activates the machine. This is not called by default, but is included
     * as most machines have this game-logical concept.
     */
    @Override
    public void onMachineActivated() {
        this.markForRenderUpdate();
    }

    /**
     * Called when the user deactivates the machine. This is not called by default, but is included
     * as most machines have this game-logical concept.
     */
    @Override
    public void onMachineDeactivated() {
        this.markForRenderUpdate();
    }

    //endregion
    //region client render support

    @Override
    protected int getUpdatedModelVariantIndex() {

        final boolean assembled = this.isMachineAssembled();

        if (assembled) {
            return 0;
        }

        return this.getPartType()
                .map(type -> {

                    switch (type) {
                        case RotorShaft:
                            return computeShaftState(this, false).ordinal();

                        case RotorBlade:
                            return computeBladeState(this, false).ordinal();

                        default:
                            return 0;
                    }
                }).orElse(0);
    }

    //endregion
    //region TileEntity

    @Override
    public void remove() {

        this.callOnLogicalClient(() -> updateNeighborsRenderState(this.getPartWorldOrFail(), this.getWorldPosition()));
        super.remove();
    }

    //endregion
    //region internals

    protected TurbineRotorComponentEntity(final TurbinePartType componentType, final TileEntityType<?> type) {

        super(type);
        this._componentType = componentType;
    }

    private static void updateNeighborsRenderState(final World world, final BlockPos position) {

        WorldHelper.getTilesFrom(world, WorldHelper.getNeighboringPositions(position))
                .filter(te -> te instanceof TurbineRotorComponentEntity)
                .map(te -> (TurbineRotorComponentEntity) te)
                .forEach(AbstractMultiblockEntity::markForRenderUpdate);
    }

    private static RotorShaftState computeShaftState(final TurbineRotorComponentEntity shaft, final boolean ignoreTurbineStatus) {

        if (!ignoreTurbineStatus && shaft.evalOnController(MultiblockTurbine::isAssembledAndActive, false)) {
            return RotorShaftState.HIDDEN;
        }

        return computeShaftStateInternal(shaft);
    }

    private static RotorShaftState computeShaftStateInternal(final TurbineRotorComponentEntity shaft) {

        final World world = shaft.getPartWorldOrFail();
        final BlockPos shaftPosition = shaft.getWorldPosition();

        final Map<Direction, BlockState> neighborsStates = Arrays.stream(CodeHelper.DIRECTIONS)
                .collect(Collectors.toMap(direction -> direction, direction -> world.getBlockState(shaftPosition.offset(direction))));

        // select an axis based on the first rotor shaft found nearby

        final Block shaftBlock = shaft.getBlockType();

        final Direction shaftDirection = neighborsStates.entrySet().stream()
                .filter(entry -> entry.getValue().getBlock() == shaftBlock)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(Direction.UP);

        // any blade around?

        final Block bladeBlock = getBladeForVariant(shaft.getMultiblockVariant().orElse(TurbineVariant.Basic));

        final Set<Direction.Axis> bladesAxis = CodeHelper.perpendicularDirections(shaftDirection).stream()
                .filter(direction -> bladeBlock == neighborsStates.get(direction).getBlock())
                .map(Direction::getAxis)
                .collect(Collectors.toSet());

        // get the final state

        return SHAFT_STATE_MAP.get(shaftDirection.getAxis()).apply(bladesAxis);
    }

    private static RotorBladeState computeBladeState(final TurbineRotorComponentEntity blade, final boolean ignoreTurbineStatus) {

        if (!ignoreTurbineStatus && blade.evalOnController(MultiblockTurbine::isAssembledAndActive, false)) {
            return RotorBladeState.HIDDEN;
        }

        return computeBladeStateInternal(blade);
    }

    private static RotorBladeState computeBladeStateInternal(final TurbineRotorComponentEntity blade) {

        final World world = blade.getPartWorldOrFail();
        final BlockPos bladePosition = blade.getWorldPosition();

        final Map<Direction, BlockState> neighborsStates = Arrays.stream(CodeHelper.DIRECTIONS)
                .collect(Collectors.toMap(direction -> direction, direction -> world.getBlockState(bladePosition.offset(direction))));

        // any shaft around?

        final Block shaftBlock = getShaftForVariant(blade.getMultiblockVariant().orElse(TurbineVariant.Basic));

        RotorBladeState candidate = neighborsStates.entrySet().stream()
                .filter(entry -> entry.getValue().getBlock() == shaftBlock)
                .map(entry -> WorldHelper.getTile(world, bladePosition.offset(entry.getKey()))
                        .filter(te -> te instanceof TurbineRotorComponentEntity)
                        .map(te -> computeShaftStateInternal((TurbineRotorComponentEntity)te))
                        .map(shaftState -> RotorBladeState.from(shaftState, entry.getKey()))
                        .orElse(RotorBladeState.HIDDEN))
                .findFirst()
                .orElse(RotorBladeState.HIDDEN);

        if (RotorBladeState.HIDDEN != candidate) {
            return candidate;
        }

        // no rotor shaft found, let's search for other blades then

        final Block bladeBlock = blade.getBlockType();

        candidate = neighborsStates.entrySet().stream()
                .filter(entry -> entry.getValue().getBlock() == bladeBlock)
                .map(entry -> computeBladeStateFromBladesChain(world, bladePosition, entry.getKey(), bladeBlock, shaftBlock))
                .filter(bladeState -> RotorBladeState.HIDDEN != bladeState)
                .findFirst()
                .orElse(RotorBladeState.HIDDEN);

        if (RotorBladeState.HIDDEN != candidate) {
            return candidate;
        }

        // give up...

        return RotorBladeState.getDefault();
    }

    private static RotorBladeState computeBladeStateFromBladesChain(final World world, BlockPos startPosition,
                                                                    final Direction direction, final Block bladeBlock,
                                                                    final Block shaftBlock) {

        do {

            final Block block = world.getBlockState(startPosition = startPosition.offset(direction)).getBlock();

            if (shaftBlock == block) {

                // found a rotor shaft

                return WorldHelper.getTile(world, startPosition)
                        .filter(te -> te instanceof TurbineRotorComponentEntity)
                        .map(te -> computeShaftStateInternal((TurbineRotorComponentEntity)te))
                        .map(shaftState -> RotorBladeState.from(shaftState, direction))
                        .orElse(RotorBladeState.HIDDEN);

            } else if (bladeBlock != block) {

                // not a rotor shaft nor a blade... give up
                break;
            }

        } while (true);

        return RotorBladeState.HIDDEN;
    }

    private static Block getShaftForVariant(final IMultiblockTurbineVariant variant) {

        if (!(variant instanceof TurbineVariant)) {
            return Blocks.AIR;
        }

        switch ((TurbineVariant)variant) {

            default:
            case Basic:
                return Content.Blocks.TURBINE_ROTORSHAFT_BASIC.get();

            case Reinforced:
                return Content.Blocks.TURBINE_ROTORSHAFT_REINFORCED.get();
        }
    }

    private static Block getBladeForVariant(final IMultiblockTurbineVariant variant) {

        if (!(variant instanceof TurbineVariant)) {
            return Blocks.AIR;
        }

        switch ((TurbineVariant)variant) {

            default:
            case Basic:
                return Content.Blocks.TURBINE_ROTORBLADE_BASIC.get();

            case Reinforced:
                return Content.Blocks.TURBINE_ROTORBLADE_REINFORCED.get();
        }
    }

    private final TurbinePartType _componentType;

    private static final Map<Direction.Axis, Function<Set<Direction.Axis>, RotorShaftState>> SHAFT_STATE_MAP;

    static {

        SHAFT_STATE_MAP = new Object2ObjectArrayMap<>(3);

        SHAFT_STATE_MAP.put(Direction.Axis.Y, set -> {

            switch (set.size()) {

                default:
                case 0:
                    return RotorShaftState.Y_NOBLADES;

                case 1:
                    return set.contains(Direction.Axis.X) ? RotorShaftState.Y_X : RotorShaftState.Y_Z;

                case 2:
                    return RotorShaftState.Y_XZ;
            }
        });

        SHAFT_STATE_MAP.put(Direction.Axis.X, set -> {

            switch (set.size()) {

                default:
                case 0:
                    return RotorShaftState.X_NOBLADES;

                case 1:
                    return set.contains(Direction.Axis.Y) ? RotorShaftState.X_Y : RotorShaftState.X_Z;

                case 2:
                    return RotorShaftState.X_YZ;
            }
        });

        SHAFT_STATE_MAP.put(Direction.Axis.Z, set -> {

            switch (set.size()) {

                default:
                case 0:
                    return RotorShaftState.Z_NOBLADES;

                case 1:
                    return set.contains(Direction.Axis.X) ? RotorShaftState.Z_X : RotorShaftState.Z_Y;

                case 2:
                    return RotorShaftState.Z_XY;
            }
        });
    }

    //endregion
}
