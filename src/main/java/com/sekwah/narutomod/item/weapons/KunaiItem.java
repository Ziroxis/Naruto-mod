package com.sekwah.narutomod.item.weapons;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.sekwah.narutomod.entity.projectile.KunaiEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class KunaiItem extends Item {

    protected Multimap<Attribute, AttributeModifier> weaponAttributes;

    public KunaiItem(Item.Properties properties) {
        super(properties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 3.0D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -1.8F, AttributeModifier.Operation.ADDITION));
        this.weaponAttributes = builder.build();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack usedItem = playerIn.getItemInHand(handIn);
        playerIn.getCooldowns().addCooldown(this, 10);
        worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (worldIn.random.nextFloat() * 0.4F + 1.2F) + 0.5F);
        if (!worldIn.isClientSide) {
            AbstractArrow kunaiEntity = createShootingEntity(worldIn, playerIn);

            kunaiEntity.shootFromRotation(playerIn, playerIn.getXRot(), playerIn.getYRot(), 0.0F, 3.0F, 1.0F);
            kunaiEntity.setBaseDamage(2);

            worldIn.addFreshEntity(kunaiEntity);
        }


        if (!playerIn.getAbilities().instabuild) {
            usedItem.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(usedItem, worldIn.isClientSide);

    }

    public AbstractArrow createShootingEntity(Level worldIn, Player playerIn) {

        KunaiEntity entity = new KunaiEntity(worldIn, playerIn);

        entity.pickup = playerIn.getAbilities().instabuild ?
                AbstractArrow.Pickup.CREATIVE_ONLY: AbstractArrow.Pickup.ALLOWED;

        return entity;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot, ItemStack stack) {
        return equipmentSlot == EquipmentSlot.MAINHAND ? weaponAttributes : super.getAttributeModifiers(equipmentSlot, stack);
    }

}
