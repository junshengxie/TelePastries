package com.mrbysco.telepastries.blocks.cake.compat;

import com.mrbysco.telepastries.blocks.cake.BlockCakeBase;
import com.mrbysco.telepastries.config.TeleConfig;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.List;

public class BlockCustomCake extends BlockCakeBase {
	public BlockCustomCake(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (!TeleConfig.SERVER.customCakeDimension.get().isEmpty()) {
			return super.use(state, worldIn, pos, player, handIn, hit);
		} else {
			if (player.getUsedItemHand() == handIn && !worldIn.isClientSide) {
				player.sendMessage(new TranslationTextComponent("telepastries.pastry.custom.unbound").withStyle(TextFormatting.RED), Util.NIL_UUID);
			}
			return ActionResultType.SUCCESS;
		}
	}

	@Override
	public IFormattableTextComponent getName() {
		return new TranslationTextComponent(this.getDescriptionId(), TeleConfig.SERVER.customCakeName.get());
	}

	@Override
	public boolean isRefillItem(ItemStack stack) {
		List<? extends String> items = TeleConfig.SERVER.customCakeRefillItem.get();
		if (items == null || items.isEmpty()) return false;
		ResourceLocation registryLocation = stack.getItem().getRegistryName();
		return registryLocation != null && items.contains(registryLocation.toString());
	}

	@Override
	public RegistryKey<World> getCakeWorld() {
		return RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(TeleConfig.SERVER.customCakeDimension.get()));
	}

	@Override
	public boolean consumeCake() {
		return TeleConfig.SERVER.consumeCustomCake.get();
	}
}
