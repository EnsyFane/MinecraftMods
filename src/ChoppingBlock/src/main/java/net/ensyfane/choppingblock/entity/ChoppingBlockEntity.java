package net.ensyfane.choppingblock.entity;

import net.ensyfane.choppingblock.ChoppingBlockMod;
import net.ensyfane.choppingblock.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChoppingBlockEntity extends BlockEntity {

    private final ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            setChanged();
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    };
    public ChoppingBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlocks.ChoppingBlockEntity.get(), pPos, pBlockState);
    }

    public ItemStackHandler getInventory()
    {
        return inventory;
    }

    public Item getItem()
    {
        return inventory.getStackInSlot(0).getItem();
    }

    public void setItem(ItemStack item)
    {
        inventory.setStackInSlot(0, item);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        var data = nbt.getCompound(ChoppingBlockMod.MOD_ID);
        this.inventory.deserializeNBT(data.getCompound("Inventory"));
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);
        var data = new CompoundTag();
        data.put("Inventory", this.inventory.serializeNBT());
        nbt.put(ChoppingBlockMod.MOD_ID, data);
    }

    @Override
    public CompoundTag getUpdateTag() {
        var tag = super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
