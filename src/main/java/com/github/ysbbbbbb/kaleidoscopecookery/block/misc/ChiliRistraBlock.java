package com.github.ysbbbbbb.kaleidoscopecookery.block.misc;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.ItemHandlerHelper;

public class ChiliRistraBlock extends Block {
   private static final int DAMAGE_COOLDOWN_TICKS = 10;
   private static final float UNDEAD_DAMAGE = 2.0F;
   private static final Map<Entity, Long> LAST_DAMAGE_TICK = new WeakHashMap<>();
   public static final BooleanProperty IS_HEAD = BooleanProperty.create("is_head");
   public static final BooleanProperty SHEARED = BooleanProperty.create("sheared");
   private static final VoxelShape AABB_HEAD = Block.box(4.0, 2.0, 4.0, 12.0, 16.0, 12.0);
   private static final VoxelShape AABB_BODY = Block.box(3.5, 0.0, 3.5, 12.5, 16.0, 12.5);

   public ChiliRistraBlock() {
      super(ModRegistrationProperties.blockProperties().mapColor(MapColor.COLOR_RED).noCollision().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY));
      this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(IS_HEAD, true)).setValue(SHEARED, false));
   }

   public InteractionResult useItemOn(
      ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult
   ) {
      if (hand != InteractionHand.MAIN_HAND) {
         return InteractionResult.TRY_WITH_EMPTY_HAND;
      } else {
         ItemStack mainHandItem = player.getMainHandItem();
         if (!mainHandItem.isEmpty() && !mainHandItem.is((Item)ModItems.RED_CHILI.get())) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
         } else {
            if ((Boolean)state.getValue(SHEARED)) {
               level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            } else {
               level.setBlock(pos, (BlockState)state.setValue(SHEARED, true), 3);
            }

            ItemStack redChili = new ItemStack((ItemLike)ModItems.RED_CHILI.get(), 3);
            if (mainHandItem.isEmpty()) {
               player.setItemInHand(InteractionHand.MAIN_HAND, redChili);
            } else {
               ItemHandlerHelper.giveItemToPlayer(player, redChili);
            }

            level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.getRandom().nextFloat() * 0.4F);
            if (level instanceof ServerLevel serverLevel) {
               serverLevel.sendParticles(
                  new BlockParticleOption(ParticleTypes.BLOCK, state), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 20, 0.25, 0.25, 0.25, 0.05
               );
            }

            return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
         }
      }
   }

   @Override
   protected void entityInside(
      BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean entityIsInside
   ) {
      if (level instanceof ServerLevel serverLevel
         && entity instanceof Mob mob
         && mob.getType().builtInRegistryHolder().is(EntityTypeTags.UNDEAD)) {
         long gameTime = serverLevel.getGameTime();
         Long lastDamageTick = LAST_DAMAGE_TICK.get(mob);
         if (lastDamageTick == null || gameTime < lastDamageTick || gameTime - lastDamageTick >= DAMAGE_COOLDOWN_TICKS) {
            if (mob.hurtServer(serverLevel, level.damageSources().magic(), UNDEAD_DAMAGE)) {
               LAST_DAMAGE_TICK.put(mob, gameTime);
            }
         }
      }
   }

   public BlockState updateShape(
      BlockState state, net.minecraft.world.level.LevelReader levelAccessor, net.minecraft.world.level.ScheduledTickAccess ticks, BlockPos currentPos, Direction direction, BlockPos neighborPos, BlockState neighborState, net.minecraft.util.RandomSource random
   ) {
      if (direction == Direction.DOWN.getOpposite() && !state.canSurvive(levelAccessor, currentPos)) {
         ticks.scheduleTick(currentPos, this, 1);
      }

      return direction == Direction.DOWN
         ? (BlockState)state.setValue(IS_HEAD, !neighborState.is(this))
         : super.updateShape(state, levelAccessor, ticks, currentPos, direction, neighborPos, neighborState, random);
   }

   public boolean canSurvive(BlockState state, LevelReader levelReader, BlockPos pos) {
      BlockPos belowPos = pos.relative(Direction.DOWN.getOpposite());
      BlockState belowState = levelReader.getBlockState(belowPos);
      return belowState.is(this) || belowState.isFaceSturdy(levelReader, belowPos, Direction.DOWN);
   }

   public void tick(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource random) {
      if (!state.canSurvive(serverLevel, pos)) {
         serverLevel.destroyBlock(pos, true);
      }
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{IS_HEAD, SHEARED});
   }

   public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
      return state.getValue(IS_HEAD) ? AABB_HEAD : AABB_BODY;
   }
}
