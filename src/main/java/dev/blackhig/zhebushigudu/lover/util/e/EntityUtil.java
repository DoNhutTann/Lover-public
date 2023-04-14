package dev.blackhig.zhebushigudu.lover.util.e;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import dev.blackhig.zhebushigudu.lover.mixin.mixins.IEntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import java.awt.Color;
import net.minecraft.util.MovementInput;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import java.util.Objects;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import dev.blackhig.zhebushigudu.lover.lover;
import net.minecraft.entity.EntityLivingBase;
import java.util.Collections;
import java.util.Iterator;
import dev.blackhig.zhebushigudu.lover.util.MathUtil;
import java.util.Collection;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockDeadBush;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.potion.Potion;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import dev.blackhig.zhebushigudu.lover.util.BlockUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.block.Block;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import dev.blackhig.zhebushigudu.lover.util.Util;

public class EntityUtil implements Util
{
    public static final Vec3d[] antiDropOffsetList;
    public static final Vec3d[] platformOffsetList;
    public static final Vec3d[] legOffsetList;
    public static final Vec3d[] OffsetList;
    public static final Vec3d[] antiStepOffsetList;
    public static final Vec3d[] antiScaffoldOffsetList;
    public static final Vec3d[] doubleLegOffsetList;
    
    public static List<BlockPos> getSphere(final BlockPos loc, final float r, final int h, final boolean hollow, final boolean sphere, final int plus_y) {
        final List<BlockPos> circleBlocks = new ArrayList<BlockPos>();
        final int cx = loc.getX();
        final int cy = loc.getY();
        final int cz = loc.getZ();
        for (int x = cx - (int)r; x <= cx + r; ++x) {
            for (int z = cz - (int)r; z <= cz + r; ++z) {
                for (int y = sphere ? (cy - (int)r) : cy; y < (sphere ? (cy + r) : ((float)(cy + h))); ++y) {
                    final double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);
                    if (dist < r * r && (!hollow || dist >= (r - 1.0f) * (r - 1.0f))) {
                        final BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleBlocks.add(l);
                    }
                }
            }
        }
        return circleBlocks;
    }
    
    public static Block isColliding(final double posX, final double posY, final double posZ) {
        Block block = null;
        if (EntityUtil.mc.player != null) {
            final AxisAlignedBB bb = (EntityUtil.mc.player.getRidingEntity() != null) ? EntityUtil.mc.player.getRidingEntity().getEntityBoundingBox().contract(0.0, 0.0, 0.0).offset(posX, posY, posZ) : EntityUtil.mc.player.getEntityBoundingBox().contract(0.0, 0.0, 0.0).offset(posX, posY, posZ);
            final int y = (int)bb.minY;
            for (int x = MathHelper.floor(bb.minX); x < MathHelper.floor(bb.maxX) + 1; ++x) {
                for (int z = MathHelper.floor(bb.minZ); z < MathHelper.floor(bb.maxZ) + 1; ++z) {
                    block = EntityUtil.mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
                }
            }
        }
        return block;
    }
    
    public static double getRelativeZ(final float yaw) {
        return MathHelper.cos(yaw * 0.017453292f);
    }
    
    public static double getRelativeX(final float yaw) {
        return MathHelper.sin(-yaw * 0.017453292f);
    }
    
    public static List<Vec3d> getVarOffsetList(final int x, final int y, final int z) {
        final ArrayList<Vec3d> offsets = new ArrayList<Vec3d>();
        offsets.add(new Vec3d((double)x, (double)y, (double)z));
        return offsets;
    }
    
    public static Vec3d[] getVarOffsets(final int x, final int y, final int z) {
        final List<Vec3d> offsets = getVarOffsetList(x, y, z);
        final Vec3d[] array = new Vec3d[offsets.size()];
        return offsets.toArray(array);
    }
    
    public static boolean basicChecksEntity(final Entity entity) {
        return entity.getName().equals(EntityUtil.mc.player.getName()) || entity.isDead;
    }
    
    public static void LocalPlayerfakeJump() {
        EntityUtil.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(EntityUtil.mc.player.posX, EntityUtil.mc.player.posY + 0.41999998688698, EntityUtil.mc.player.posZ, EntityUtil.mc.player.onGround));
        EntityUtil.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(EntityUtil.mc.player.posX, EntityUtil.mc.player.posY + 0.7531999805211997, EntityUtil.mc.player.posZ, EntityUtil.mc.player.onGround));
        EntityUtil.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(EntityUtil.mc.player.posX, EntityUtil.mc.player.posY + 1.00133597911214, EntityUtil.mc.player.posZ, EntityUtil.mc.player.onGround));
        EntityUtil.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(EntityUtil.mc.player.posX, EntityUtil.mc.player.posY + 1.15610926093821, EntityUtil.mc.player.posZ, EntityUtil.mc.player.onGround));
    }
    
    public static BlockPos getLocalPlayerPosFloored() {
        return new BlockPos(Math.floor(EntityUtil.mc.player.posX), Math.floor(EntityUtil.mc.player.posY), Math.floor(EntityUtil.mc.player.posZ));
    }
    
    public static boolean isPlayerInHole() {
        final BlockPos blockPos = getLocalPlayerPosFloored();
        final IBlockState blockState = EntityUtil.mc.world.getBlockState(blockPos);
        if (blockState.getBlock() != Blocks.AIR) {
            return false;
        }
        if (EntityUtil.mc.world.getBlockState(blockPos.up()).getBlock() != Blocks.AIR) {
            return false;
        }
        if (EntityUtil.mc.world.getBlockState(blockPos.down()).getBlock() == Blocks.AIR) {
            return false;
        }
        final BlockPos[] touchingBlocks = { blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west() };
        int validHorizontalBlocks = 0;
        for (final BlockPos touching : touchingBlocks) {
            final IBlockState touchingState = EntityUtil.mc.world.getBlockState(touching);
            if (touchingState.getBlock() != Blocks.AIR && touchingState.isFullBlock()) {
                ++validHorizontalBlocks;
            }
        }
        return validHorizontalBlocks >= 4;
    }
    
    public static boolean getSurroundWeakness(final Vec3d pos, final int feetMine, final int render) {
        switch (feetMine) {
            case 1: {
                final BlockPos raytrace = new BlockPos(pos);
                if (!BlockUtil.canBlockBeSeen(raytrace.getX() - 2, raytrace.getY(), raytrace.getZ()) && Math.sqrt(EntityUtil.mc.player.getDistanceSq((double)(raytrace.getX() - 2), (double)raytrace.getY(), (double)raytrace.getZ())) > 3.0) {
                    return false;
                }
                final Block block = EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(-2, 1, 0)).getBlock();
                final Block blocka;
                if ((block != Blocks.AIR && block != Blocks.FIRE) || ((blocka = EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(-2, 0, 0)).getBlock()) != Blocks.AIR && blocka != Blocks.FIRE)) {
                    break;
                }
                final Block blockb;
                if ((blockb = EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(-2, -1, 0)).getBlock()) != Blocks.OBSIDIAN && blockb != Blocks.BEDROCK) {
                    break;
                }
                if (EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(-1, 0, 0)).getBlock() == Blocks.BEDROCK) {
                    break;
                }
                return true;
            }
            case 2: {
                final BlockPos raytrace = new BlockPos(pos);
                if (!BlockUtil.canBlockBeSeen(raytrace.getX() + 2, raytrace.getY(), raytrace.getZ()) && Math.sqrt(EntityUtil.mc.player.getDistanceSq((double)(raytrace.getX() + 2), (double)raytrace.getY(), (double)raytrace.getZ())) > 3.0) {
                    return false;
                }
                final Block block = EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(2, 1, 0)).getBlock();
                final Block blocka;
                if ((block != Blocks.AIR && block != Blocks.FIRE) || ((blocka = EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(2, 0, 0)).getBlock()) != Blocks.AIR && blocka != Blocks.FIRE)) {
                    break;
                }
                final Block blockb;
                if ((blockb = EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(2, -1, 0)).getBlock()) != Blocks.OBSIDIAN && blockb != Blocks.BEDROCK) {
                    break;
                }
                if (EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(1, 0, 0)).getBlock() == Blocks.BEDROCK) {
                    break;
                }
                return true;
            }
            case 3: {
                final BlockPos raytrace = new BlockPos(pos);
                if (!BlockUtil.canBlockBeSeen(raytrace.getX(), raytrace.getY(), raytrace.getZ() - 2) && Math.sqrt(EntityUtil.mc.player.getDistanceSq((double)raytrace.getX(), (double)raytrace.getY(), (double)(raytrace.getZ() - 2))) > 3.0) {
                    return false;
                }
                final Block block = EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(0, 1, -2)).getBlock();
                final Block blocka;
                if ((block != Blocks.AIR && block != Blocks.FIRE) || ((blocka = EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(0, 0, -2)).getBlock()) != Blocks.AIR && blocka != Blocks.FIRE)) {
                    break;
                }
                final Block blockb;
                if ((blockb = EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(0, -1, -2)).getBlock()) != Blocks.OBSIDIAN && blockb != Blocks.BEDROCK) {
                    break;
                }
                if (EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(0, 0, -1)).getBlock() == Blocks.BEDROCK) {
                    break;
                }
                return true;
            }
            case 4: {
                final BlockPos raytrace = new BlockPos(pos);
                if (!BlockUtil.canBlockBeSeen(raytrace.getX(), raytrace.getY(), raytrace.getZ() + 2) && Math.sqrt(EntityUtil.mc.player.getDistanceSq((double)raytrace.getX(), (double)raytrace.getY(), (double)(raytrace.getZ() + 2))) > 3.0) {
                    return false;
                }
                final Block block = EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(0, 1, 2)).getBlock();
                final Block blocka;
                if ((block != Blocks.AIR && block != Blocks.FIRE) || ((blocka = EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(0, 0, 2)).getBlock()) != Blocks.AIR && blocka != Blocks.FIRE)) {
                    break;
                }
                final Block blockb;
                if ((blockb = EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(0, -1, 2)).getBlock()) != Blocks.OBSIDIAN && blockb != Blocks.BEDROCK) {
                    break;
                }
                if (EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(0, 0, 1)).getBlock() == Blocks.BEDROCK) {
                    break;
                }
                return true;
            }
            case 5: {
                final BlockPos raytrace2 = new BlockPos(pos);
                if (!BlockUtil.canBlockBeSeen(raytrace2.getX() - 1, raytrace2.getY(), raytrace2.getZ()) && Math.sqrt(EntityUtil.mc.player.getDistanceSq((double)(raytrace2.getX() - 1), (double)raytrace2.getY(), (double)raytrace2.getZ())) > 3.0) {
                    return false;
                }
                final Block block2 = EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(-1, 1, 0)).getBlock();
                if (block2 != Blocks.AIR && block2 != Blocks.FIRE) {
                    break;
                }
                if (EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(-1, 0, 0)).getBlock() == Blocks.BEDROCK) {
                    break;
                }
                return true;
            }
            case 6: {
                final BlockPos raytrace2 = new BlockPos(pos);
                if (!BlockUtil.canBlockBeSeen(raytrace2.getX() + 1, raytrace2.getY(), raytrace2.getZ()) && Math.sqrt(EntityUtil.mc.player.getDistanceSq((double)(raytrace2.getX() + 1), (double)raytrace2.getY(), (double)raytrace2.getZ())) > 3.0) {
                    return false;
                }
                final Block block2 = EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(1, 1, 0)).getBlock();
                if (block2 != Blocks.AIR && block2 != Blocks.FIRE) {
                    break;
                }
                if (EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(1, 0, 0)).getBlock() == Blocks.BEDROCK) {
                    break;
                }
                return true;
            }
            case 7: {
                final BlockPos raytrace2 = new BlockPos(pos);
                if (!BlockUtil.canBlockBeSeen(raytrace2.getX(), raytrace2.getY(), raytrace2.getZ() - 1) && Math.sqrt(EntityUtil.mc.player.getDistanceSq((double)raytrace2.getX(), (double)raytrace2.getY(), (double)(raytrace2.getZ() - 1))) > 3.0) {
                    return false;
                }
                final Block block2 = EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(0, 1, -1)).getBlock();
                if (block2 != Blocks.AIR && block2 != Blocks.FIRE) {
                    break;
                }
                if (EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(0, 0, -1)).getBlock() == Blocks.BEDROCK) {
                    break;
                }
                return true;
            }
            case 8: {
                final BlockPos raytrace2 = new BlockPos(pos);
                if (!BlockUtil.canBlockBeSeen(raytrace2.getX(), raytrace2.getY(), raytrace2.getZ() + 1) && Math.sqrt(EntityUtil.mc.player.getDistanceSq((double)raytrace2.getX(), (double)raytrace2.getY(), (double)(raytrace2.getZ() + 1))) > 3.0) {
                    return false;
                }
                final Block block2 = EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(0, 1, 1)).getBlock();
                if (block2 != Blocks.AIR && block2 != Blocks.FIRE) {
                    break;
                }
                if (EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(0, 0, 1)).getBlock() == Blocks.BEDROCK) {
                    break;
                }
                return true;
            }
        }
        switch (render) {
            case 1: {
                final Block block3 = EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(-2, 1, 0)).getBlock();
                final Block blocka2;
                final Block blockb2;
                return (block3 == Blocks.AIR || block3 == Blocks.FIRE) && ((blocka2 = EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(-2, 0, 0)).getBlock()) == Blocks.AIR || blocka2 == Blocks.FIRE) && ((blockb2 = EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(-2, -1, 0)).getBlock()) == Blocks.OBSIDIAN || blockb2 == Blocks.BEDROCK) && EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(-1, 0, 0)).getBlock() != Blocks.BEDROCK;
            }
            case 2: {
                final Block block3 = EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(2, 1, 0)).getBlock();
                final Block blocka2;
                final Block blockb2;
                return (block3 == Blocks.AIR || block3 == Blocks.FIRE) && ((blocka2 = EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(2, 0, 0)).getBlock()) == Blocks.AIR || blocka2 == Blocks.FIRE) && ((blockb2 = EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(2, -1, 0)).getBlock()) == Blocks.OBSIDIAN || blockb2 == Blocks.BEDROCK) && EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(1, 0, 0)).getBlock() != Blocks.BEDROCK;
            }
            case 3: {
                final Block block3 = EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(0, 1, -2)).getBlock();
                final Block blocka2;
                final Block blockb2;
                return (block3 == Blocks.AIR || block3 == Blocks.FIRE) && ((blocka2 = EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(0, 0, -2)).getBlock()) == Blocks.AIR || blocka2 == Blocks.FIRE) && ((blockb2 = EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(0, -1, -2)).getBlock()) == Blocks.OBSIDIAN || blockb2 == Blocks.BEDROCK) && EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(0, 0, -1)).getBlock() != Blocks.BEDROCK;
            }
            case 4: {
                final Block block3 = EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(0, 1, 2)).getBlock();
                final Block blocka2;
                final Block blockb2;
                return (block3 == Blocks.AIR || block3 == Blocks.FIRE) && ((blocka2 = EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(0, 0, 2)).getBlock()) == Blocks.AIR || blocka2 == Blocks.FIRE) && ((blockb2 = EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(0, -1, 2)).getBlock()) == Blocks.OBSIDIAN || blockb2 == Blocks.BEDROCK) && EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(0, 0, 1)).getBlock() != Blocks.BEDROCK;
            }
            case 5: {
                final Block block4 = EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(-1, 1, 0)).getBlock();
                return (block4 == Blocks.AIR || block4 == Blocks.FIRE) && EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(-1, 0, 0)).getBlock() != Blocks.BEDROCK;
            }
            case 6: {
                final Block block4 = EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(1, 1, 0)).getBlock();
                return (block4 == Blocks.AIR || block4 == Blocks.FIRE) && EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(1, 0, 0)).getBlock() != Blocks.BEDROCK;
            }
            case 7: {
                final Block block4 = EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(0, 1, -1)).getBlock();
                return (block4 == Blocks.AIR || block4 == Blocks.FIRE) && EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(0, 0, -1)).getBlock() != Blocks.BEDROCK;
            }
            case 8: {
                final Block block4 = EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(0, 1, 1)).getBlock();
                return (block4 == Blocks.AIR || block4 == Blocks.FIRE) && EntityUtil.mc.world.getBlockState(new BlockPos(pos).add(0, 0, 1)).getBlock() != Blocks.BEDROCK;
            }
            default: {
                return false;
            }
        }
    }
    
    public static boolean isOnLiquid() {
        final double y = EntityUtil.mc.player.posY - 0.03;
        for (int x = MathHelper.floor(EntityUtil.mc.player.posX); x < MathHelper.ceil(EntityUtil.mc.player.posX); ++x) {
            for (int z = MathHelper.floor(EntityUtil.mc.player.posZ); z < MathHelper.ceil(EntityUtil.mc.player.posZ); ++z) {
                final BlockPos pos = new BlockPos(x, MathHelper.floor(y), z);
                if (EntityUtil.mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static void resetTimer() {
        EntityUtil.mc.timer.tickLength = 50.0f;
    }
    
    public static boolean isInLiquid() {
        if (EntityUtil.mc.player == null) {
            return false;
        }
        if (EntityUtil.mc.player.fallDistance >= 3.0f) {
            return false;
        }
        boolean inLiquid = false;
        final AxisAlignedBB bb = (EntityUtil.mc.player.getRidingEntity() != null) ? EntityUtil.mc.player.getRidingEntity().getEntityBoundingBox() : EntityUtil.mc.player.getEntityBoundingBox();
        final int y = (int)bb.minY;
        for (int x = MathHelper.floor(bb.minX); x < MathHelper.floor(bb.maxX) + 1; ++x) {
            for (int z = MathHelper.floor(bb.minZ); z < MathHelper.floor(bb.maxZ) + 1; ++z) {
                final Block block = EntityUtil.mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (!(block instanceof BlockAir)) {
                    if (!(block instanceof BlockLiquid)) {
                        return false;
                    }
                    inLiquid = true;
                }
            }
        }
        return inLiquid;
    }
    
    public static void attackEntity(final Entity entity, final boolean packet, final EnumHand hand) {
        if (packet) {
            EntityUtil.mc.player.connection.sendPacket((Packet)new CPacketUseEntity(entity));
        }
        else {
            EntityUtil.mc.playerController.attackEntity((EntityPlayer)EntityUtil.mc.player, entity);
        }
        EntityUtil.mc.player.swingArm(hand);
    }
    
    public static void attackEntity(final Entity entity, final boolean packet, final boolean swingArm) {
        if (packet) {
            EntityUtil.mc.player.connection.sendPacket((Packet)new CPacketUseEntity(entity));
        }
        else {
            EntityUtil.mc.playerController.attackEntity((EntityPlayer)EntityUtil.mc.player, entity);
        }
        if (swingArm) {
            EntityUtil.mc.player.swingArm(EnumHand.MAIN_HAND);
        }
    }
    
    public static void attackEntityfos(final Entity entity, final boolean packet, final boolean swingArm) {
        if (packet) {
            EntityUtil.mc.player.connection.sendPacket((Packet)new CPacketUseEntity(entity));
        }
        else {
            EntityUtil.mc.playerController.attackEntity((EntityPlayer)EntityUtil.mc.player, entity);
        }
        if (swingArm) {
            EntityUtil.mc.player.swingArm(EnumHand.OFF_HAND);
        }
    }
    
    public static void setTimer(final float speed) {
        EntityUtil.mc.timer.tickLength = 50.0f / speed;
    }
    
    public static Vec3d interpolateEntity(final Entity entity, final float time) {
        return new Vec3d(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * time, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * time, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * time);
    }
    
    public static Vec3d getInterpolatedPos(final Entity entity, final float partialTicks) {
        return new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).add(getInterpolatedAmount(entity, partialTicks));
    }
    
    public static Vec3d getInterpolatedRenderPos(final Entity entity, final float partialTicks) {
        return getInterpolatedPos(entity, partialTicks).subtract(EntityUtil.mc.getRenderManager().renderPosX, EntityUtil.mc.getRenderManager().renderPosY, EntityUtil.mc.getRenderManager().renderPosZ);
    }
    
    public static Vec3d getInterpolatedRenderPos(final Vec3d vec) {
        return new Vec3d(vec.x, vec.y, vec.z).subtract(EntityUtil.mc.getRenderManager().renderPosX, EntityUtil.mc.getRenderManager().renderPosY, EntityUtil.mc.getRenderManager().renderPosZ);
    }
    
    public static Vec3d getInterpolatedAmount(final Entity entity, final double x, final double y, final double z) {
        return new Vec3d((entity.posX - entity.lastTickPosX) * x, (entity.posY - entity.lastTickPosY) * y, (entity.posZ - entity.lastTickPosZ) * z);
    }
    
    public static Vec3d getInterpolatedAmount(final Entity entity, final Vec3d vec) {
        return getInterpolatedAmount(entity, vec.x, vec.y, vec.z);
    }
    
    public static Vec3d getInterpolatedAmount(final Entity entity, final float partialTicks) {
        return getInterpolatedAmount(entity, partialTicks, partialTicks, partialTicks);
    }
    
    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (EntityUtil.mc.player != null && EntityUtil.mc.player.isPotionActive(Potion.getPotionById(1))) {
            final int amplifier = EntityUtil.mc.player.getActivePotionEffect(Potion.getPotionById(1)).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }
    
    public static boolean isPassive(final Entity entity) {
        return (!(entity instanceof EntityWolf) || !((EntityWolf)entity).isAngry()) && (entity instanceof EntityAgeable || entity instanceof EntityAmbientCreature || entity instanceof EntitySquid || (entity instanceof EntityIronGolem && ((EntityIronGolem)entity).getRevengeTarget() == null));
    }
    
    public static boolean isSafe(final Entity entity, final int height, final boolean floor, final boolean face) {
        return getUnsafeBlocks(entity, height, floor).size() == 0;
    }
    
    public static boolean isSafe(final Entity entity, final int height, final boolean floor) {
        return getUnsafeBlocks(entity, height, floor).size() == 0;
    }
    
    public static boolean stopSneaking(final boolean isSneaking) {
        if (isSneaking && EntityUtil.mc.player != null) {
            EntityUtil.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)EntityUtil.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        }
        return false;
    }
    
    public static boolean isSafe(final Entity entity) {
        return isSafe(entity, 0, false, true);
    }
    
    public static BlockPos getPlayerPos(final EntityPlayer player) {
        return new BlockPos(Math.floor(player.posX), Math.floor(player.posY), Math.floor(player.posZ));
    }
    
    public static List<Vec3d> getUnsafeBlocks(final Entity entity, final int height, final boolean floor) {
        return getUnsafeBlocksFromVec3d(entity.getPositionVector(), height, floor);
    }
    
    public static boolean isMobAggressive(final Entity entity) {
        if (entity instanceof EntityPigZombie) {
            if (((EntityPigZombie)entity).isArmsRaised() || ((EntityPigZombie)entity).isAngry()) {
                return true;
            }
        }
        else {
            if (entity instanceof EntityWolf) {
                return ((EntityWolf)entity).isAngry() && !EntityUtil.mc.player.equals((Object)((EntityWolf)entity).getOwner());
            }
            if (entity instanceof EntityEnderman) {
                return ((EntityEnderman)entity).isScreaming();
            }
        }
        return isHostileMob(entity);
    }
    
    public static boolean isNeutralMob(final Entity entity) {
        return entity instanceof EntityPigZombie || entity instanceof EntityWolf || entity instanceof EntityEnderman;
    }
    
    public static boolean isProjectile(final Entity entity) {
        return entity instanceof EntityShulkerBullet || entity instanceof EntityFireball;
    }
    
    public static boolean isVehicle(final Entity entity) {
        return entity instanceof EntityBoat || entity instanceof EntityMinecart;
    }
    
    public static boolean isFriendlyMob(final Entity entity) {
        return (entity.isCreatureType(EnumCreatureType.CREATURE, false) && !isNeutralMob(entity)) || entity.isCreatureType(EnumCreatureType.AMBIENT, false) || entity instanceof EntityVillager || entity instanceof EntityIronGolem || (isNeutralMob(entity) && !isMobAggressive(entity));
    }
    
    public static boolean isHostileMob(final Entity entity) {
        return entity.isCreatureType(EnumCreatureType.MONSTER, false) && !isNeutralMob(entity);
    }
    
    public static List<Vec3d> getUnsafeBlocksFromVec3d(final Vec3d pos, final int height, final boolean floor) {
        final ArrayList<Vec3d> vec3ds = new ArrayList<Vec3d>();
        for (final Vec3d vector : getOffsets(height, floor)) {
            final BlockPos targetPos = new BlockPos(pos).add(vector.x, vector.y, vector.z);
            final Block block = EntityUtil.mc.world.getBlockState(targetPos).getBlock();
            if (block instanceof BlockAir || block instanceof BlockLiquid || block instanceof BlockTallGrass || block instanceof BlockFire || block instanceof BlockDeadBush || block instanceof BlockSnow) {
                vec3ds.add(vector);
            }
        }
        return vec3ds;
    }
    
    public static boolean isInHole(final Entity entity) {
        return isBlockValid(new BlockPos(entity.posX, entity.posY, entity.posZ));
    }
    
    public static boolean isBlockValid(final BlockPos blockPos) {
        return isBedrockHole(blockPos) || isObbyHole(blockPos) || isBothHole(blockPos);
    }
    
    public static boolean isObbyHole(final BlockPos blockPos) {
        final BlockPos[] touchingBlocks;
        final BlockPos[] array2;
        final BlockPos[] array = array2 = (touchingBlocks = new BlockPos[] { blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down() });
        for (final BlockPos pos : array2) {
            final IBlockState touchingState = EntityUtil.mc.world.getBlockState(pos);
            if (touchingState.getBlock() == Blocks.AIR || touchingState.getBlock() != Blocks.OBSIDIAN) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isBedrockHole(final BlockPos blockPos) {
        final BlockPos[] touchingBlocks;
        final BlockPos[] array2;
        final BlockPos[] array = array2 = (touchingBlocks = new BlockPos[] { blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down() });
        for (final BlockPos pos : array2) {
            final IBlockState touchingState = EntityUtil.mc.world.getBlockState(pos);
            if (touchingState.getBlock() == Blocks.AIR || touchingState.getBlock() != Blocks.BEDROCK) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isBothHole(final BlockPos blockPos) {
        final BlockPos[] touchingBlocks;
        final BlockPos[] array2;
        final BlockPos[] array = array2 = (touchingBlocks = new BlockPos[] { blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down() });
        for (final BlockPos pos : array2) {
            final IBlockState touchingState = EntityUtil.mc.world.getBlockState(pos);
            if (touchingState.getBlock() == Blocks.AIR || (touchingState.getBlock() != Blocks.BEDROCK && touchingState.getBlock() != Blocks.OBSIDIAN)) {
                return false;
            }
        }
        return true;
    }
    
    public static Vec3d[] getUnsafeBlockArray(final Entity entity, final int height, final boolean floor) {
        final List<Vec3d> list = getUnsafeBlocks(entity, height, floor);
        final Vec3d[] array = new Vec3d[list.size()];
        return list.toArray(array);
    }
    
    public static Vec3d[] getUnsafeBlockArrayFromVec3d(final Vec3d pos, final int height, final boolean floor) {
        final List<Vec3d> list = getUnsafeBlocksFromVec3d(pos, height, floor);
        final Vec3d[] array = new Vec3d[list.size()];
        return list.toArray(array);
    }
    
    public static double getDst(final Vec3d vec) {
        return EntityUtil.mc.player.getPositionVector().distanceTo(vec);
    }
    
    public static boolean isTrapped(final EntityPlayer player, final boolean antiScaffold, final boolean antiStep, final boolean legs, final boolean platform, final boolean antiDrop) {
        return getUntrappedBlocks(player, antiScaffold, antiStep, legs, platform, antiDrop).size() == 0;
    }
    
    public static boolean isTrappedExtended(final int extension, final EntityPlayer player, final boolean antiScaffold, final boolean antiStep, final boolean legs, final boolean platform, final boolean antiDrop, final boolean raytrace) {
        return getUntrappedBlocksExtended(extension, player, antiScaffold, antiStep, legs, platform, antiDrop, raytrace).size() == 0;
    }
    
    public static List<Vec3d> getUntrappedBlocks(final EntityPlayer player, final boolean antiScaffold, final boolean antiStep, final boolean legs, final boolean platform, final boolean antiDrop) {
        final ArrayList<Vec3d> vec3ds = new ArrayList<Vec3d>();
        if (!antiStep && getUnsafeBlocks((Entity)player, 2, false).size() == 4) {
            vec3ds.addAll(getUnsafeBlocks((Entity)player, 2, false));
        }
        for (int i = 0; i < getTrapOffsets(antiScaffold, antiStep, legs, platform, antiDrop).length; ++i) {
            final Vec3d vector = getTrapOffsets(antiScaffold, antiStep, legs, platform, antiDrop)[i];
            final BlockPos targetPos = new BlockPos(player.getPositionVector()).add(vector.x, vector.y, vector.z);
            final Block block = EntityUtil.mc.world.getBlockState(targetPos).getBlock();
            if (block instanceof BlockAir || block instanceof BlockLiquid || block instanceof BlockTallGrass || block instanceof BlockFire || block instanceof BlockDeadBush || block instanceof BlockSnow) {
                vec3ds.add(vector);
            }
        }
        return vec3ds;
    }
    
    public static boolean isInWater(final Entity entity) {
        if (entity == null) {
            return false;
        }
        final double y = entity.posY + 0.01;
        for (int x = MathHelper.floor(entity.posX); x < MathHelper.ceil(entity.posX); ++x) {
            for (int z = MathHelper.floor(entity.posZ); z < MathHelper.ceil(entity.posZ); ++z) {
                final BlockPos pos = new BlockPos(x, (int)y, z);
                if (EntityUtil.mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean isDrivenByPlayer(final Entity entityIn) {
        return EntityUtil.mc.player != null && entityIn != null && entityIn.equals((Object)EntityUtil.mc.player.getRidingEntity());
    }
    
    public static boolean isPlayer(final Entity entity) {
        return entity instanceof EntityPlayer;
    }
    
    public static boolean isAboveWater(final Entity entity) {
        return isAboveWater(entity, false);
    }
    
    public static boolean isAboveWater(final Entity entity, final boolean packet) {
        if (entity == null) {
            return false;
        }
        final double y = entity.posY - (packet ? 0.03 : (isPlayer(entity) ? 0.2 : 0.5));
        for (int x = MathHelper.floor(entity.posX); x < MathHelper.ceil(entity.posX); ++x) {
            for (int z = MathHelper.floor(entity.posZ); z < MathHelper.ceil(entity.posZ); ++z) {
                final BlockPos pos = new BlockPos(x, MathHelper.floor(y), z);
                if (EntityUtil.mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static double[] calculateLookAt(final double px, final double py, final double pz, final EntityPlayer me) {
        double dirx = me.posX - px;
        double diry = me.posY - py;
        double dirz = me.posZ - pz;
        final double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);
        dirx /= len;
        diry /= len;
        dirz /= len;
        double pitch = Math.asin(diry);
        double yaw = Math.atan2(dirz, dirx);
        pitch = pitch * 180.0 / 3.141592653589793;
        yaw = yaw * 180.0 / 3.141592653589793;
        yaw += 90.0;
        return new double[] { yaw, pitch };
    }
    
    public static List<Vec3d> getUntrappedBlocksExtended(final int extension, final EntityPlayer player, final boolean antiScaffold, final boolean antiStep, final boolean legs, final boolean platform, final boolean antiDrop, final boolean raytrace) {
        final ArrayList<Vec3d> placeTargets = new ArrayList<Vec3d>();
        if (extension == 1) {
            placeTargets.addAll(targets(player.getPositionVector(), antiScaffold, antiStep, legs, platform, antiDrop, raytrace));
        }
        else {
            int extend = 1;
            for (final Vec3d vec3d : MathUtil.getBlockBlocks((Entity)player)) {
                if (extend > extension) {
                    break;
                }
                placeTargets.addAll(targets(vec3d, antiScaffold, antiStep, legs, platform, antiDrop, raytrace));
                ++extend;
            }
        }
        final ArrayList<Vec3d> removeList = new ArrayList<Vec3d>();
        for (final Vec3d vec3d : placeTargets) {
            final BlockPos pos = new BlockPos(vec3d);
            if (BlockUtil.isPositionPlaceable(pos, raytrace) != -1) {
                continue;
            }
            removeList.add(vec3d);
        }
        for (final Vec3d vec3d : removeList) {
            placeTargets.remove(vec3d);
        }
        return placeTargets;
    }
    
    public static List<Vec3d> targets(final Vec3d vec3d, final boolean antiScaffold, final boolean antiStep, final boolean legs, final boolean platform, final boolean antiDrop, final boolean raytrace) {
        final ArrayList<Vec3d> placeTargets = new ArrayList<Vec3d>();
        if (antiDrop) {
            Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, EntityUtil.antiDropOffsetList));
        }
        if (platform) {
            Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, EntityUtil.platformOffsetList));
        }
        if (legs) {
            Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, EntityUtil.legOffsetList));
        }
        Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, EntityUtil.OffsetList));
        if (antiStep) {
            Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, EntityUtil.antiStepOffsetList));
        }
        else {
            final List<Vec3d> vec3ds = getUnsafeBlocksFromVec3d(vec3d, 2, false);
            if (vec3ds.size() == 4) {
                for (final Vec3d vector : vec3ds) {
                    final BlockPos position = new BlockPos(vec3d).add(vector.x, vector.y, vector.z);
                    switch (BlockUtil.isPositionPlaceable(position, raytrace)) {
                        case -1:
                        case 1:
                        case 2: {
                            continue;
                        }
                        case 3: {
                            placeTargets.add(vec3d.add(vector));
                            break;
                        }
                    }
                    if (antiScaffold) {
                        Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, EntityUtil.antiScaffoldOffsetList));
                    }
                    return placeTargets;
                }
            }
        }
        if (antiScaffold) {
            Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, EntityUtil.antiScaffoldOffsetList));
        }
        return placeTargets;
    }
    
    public static List<Vec3d> getOffsetList(final int y, final boolean floor) {
        final ArrayList<Vec3d> offsets = new ArrayList<Vec3d>();
        offsets.add(new Vec3d(-1.0, (double)y, 0.0));
        offsets.add(new Vec3d(1.0, (double)y, 0.0));
        offsets.add(new Vec3d(0.0, (double)y, -1.0));
        offsets.add(new Vec3d(0.0, (double)y, 1.0));
        if (floor) {
            offsets.add(new Vec3d(0.0, (double)(y - 1), 0.0));
        }
        return offsets;
    }
    
    public static Vec3d[] getOffsets(final int y, final boolean floor) {
        final List<Vec3d> offsets = getOffsetList(y, floor);
        final Vec3d[] array = new Vec3d[offsets.size()];
        return offsets.toArray(array);
    }
    
    public static Vec3d[] getTrapOffsets(final boolean antiScaffold, final boolean antiStep, final boolean legs, final boolean platform, final boolean antiDrop) {
        final List<Vec3d> offsets = getTrapOffsetsList(antiScaffold, antiStep, legs, platform, antiDrop);
        final Vec3d[] array = new Vec3d[offsets.size()];
        return offsets.toArray(array);
    }
    
    public static List<Vec3d> getTrapOffsetsList(final boolean antiScaffold, final boolean antiStep, final boolean legs, final boolean platform, final boolean antiDrop) {
        final ArrayList<Vec3d> offsets = new ArrayList<Vec3d>(getOffsetList(1, false));
        offsets.add(new Vec3d(0.0, 2.0, 0.0));
        if (antiScaffold) {
            offsets.add(new Vec3d(0.0, 3.0, 0.0));
        }
        if (antiStep) {
            offsets.addAll(getOffsetList(2, false));
        }
        if (legs) {
            offsets.addAll(getOffsetList(0, false));
        }
        if (platform) {
            offsets.addAll(getOffsetList(-1, false));
            offsets.add(new Vec3d(0.0, -1.0, 0.0));
        }
        if (antiDrop) {
            offsets.add(new Vec3d(0.0, -2.0, 0.0));
        }
        return offsets;
    }
    
    public static Vec3d[] getHeightOffsets(final int min, final int max) {
        final ArrayList<Vec3d> offsets = new ArrayList<Vec3d>();
        for (int i = min; i <= max; ++i) {
            offsets.add(new Vec3d(0.0, (double)i, 0.0));
        }
        final Vec3d[] array = new Vec3d[offsets.size()];
        return offsets.toArray(array);
    }
    
    public static BlockPos getRoundedBlockPos(final Entity entity) {
        return new BlockPos(MathUtil.roundVec(entity.getPositionVector(), 0));
    }
    
    public static boolean isLiving(final Entity entity) {
        return entity instanceof EntityLivingBase;
    }
    
    public static boolean isAlive(final Entity entity) {
        return isLiving(entity) && !entity.isDead && ((EntityLivingBase)entity).getHealth() > 0.0f;
    }
    
    public static boolean isDead(final Entity entity) {
        return !isAlive(entity);
    }
    
    public static float getHealth(final Entity entity) {
        if (isLiving(entity)) {
            final EntityLivingBase livingBase = (EntityLivingBase)entity;
            return livingBase.getHealth() + livingBase.getAbsorptionAmount();
        }
        return 0.0f;
    }
    
    public static float getHealth(final Entity entity, final boolean absorption) {
        if (isLiving(entity)) {
            final EntityLivingBase livingBase = (EntityLivingBase)entity;
            return livingBase.getHealth() + (absorption ? livingBase.getAbsorptionAmount() : 0.0f);
        }
        return 0.0f;
    }
    
    public static boolean canEntityFeetBeSeen(final Entity entityIn) {
        return EntityUtil.mc.world.rayTraceBlocks(new Vec3d(EntityUtil.mc.player.posX, EntityUtil.mc.player.posX + EntityUtil.mc.player.getEyeHeight(), EntityUtil.mc.player.posZ), new Vec3d(entityIn.posX, entityIn.posY, entityIn.posZ), false, true, false) == null;
    }
    
    public static boolean isntValid(final Entity entity, final double range) {
        return entity == null || isDead(entity) || entity.equals((Object)EntityUtil.mc.player) || (entity instanceof EntityPlayer && lover.friendManager.isFriend(entity.getName())) || EntityUtil.mc.player.getDistanceSq(entity) > MathUtil.square(range);
    }
    
    public static boolean isValid(final Entity entity, final double range) {
        return !isntValid(entity, range);
    }
    
    public static boolean holdingWeapon(final EntityPlayer player) {
        return player.getHeldItemMainhand().getItem() instanceof ItemSword || player.getHeldItemMainhand().getItem() instanceof ItemAxe;
    }
    
    public static double getMaxSpeed() {
        double maxModifier = 0.2873;
        if (EntityUtil.mc.player.isPotionActive((Potion)Objects.requireNonNull(Potion.getPotionById(1)))) {
            maxModifier *= 1.0 + 0.2 * (Objects.requireNonNull(EntityUtil.mc.player.getActivePotionEffect((Potion)Objects.requireNonNull(Potion.getPotionById(1)))).getAmplifier() + 1);
        }
        return maxModifier;
    }
    
    public static void mutliplyEntitySpeed(final Entity entity, final double multiplier) {
        if (entity != null) {
            entity.motionX *= multiplier;
            entity.motionZ *= multiplier;
        }
    }
    
    public static boolean isEntityMoving(final Entity entity) {
        if (entity == null) {
            return false;
        }
        if (entity instanceof EntityPlayer) {
            return EntityUtil.mc.gameSettings.keyBindForward.isKeyDown() || EntityUtil.mc.gameSettings.keyBindBack.isKeyDown() || EntityUtil.mc.gameSettings.keyBindLeft.isKeyDown() || EntityUtil.mc.gameSettings.keyBindRight.isKeyDown();
        }
        return entity.motionX != 0.0 || entity.motionY != 0.0 || entity.motionZ != 0.0;
    }
    
    public static double getEntitySpeed(final Entity entity) {
        if (entity != null) {
            final double distTraveledLastTickX = entity.posX - entity.prevPosX;
            final double distTraveledLastTickZ = entity.posZ - entity.prevPosZ;
            final double speed = MathHelper.sqrt(distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ);
            return speed * 20.0;
        }
        return 0.0;
    }
    
    public static boolean is32k(final ItemStack stack) {
        return EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, stack) >= 1000;
    }
    
    public static void moveEntityStrafe(final double speed, final Entity entity) {
        if (entity != null) {
            final MovementInput movementInput = EntityUtil.mc.player.movementInput;
            double forward = movementInput.moveForward;
            double strafe = movementInput.moveStrafe;
            float yaw = EntityUtil.mc.player.rotationYaw;
            if (forward == 0.0 && strafe == 0.0) {
                entity.motionX = 0.0;
                entity.motionZ = 0.0;
            }
            else {
                if (forward != 0.0) {
                    if (strafe > 0.0) {
                        yaw += ((forward > 0.0) ? -45 : 45);
                    }
                    else if (strafe < 0.0) {
                        yaw += ((forward > 0.0) ? 45 : -45);
                    }
                    strafe = 0.0;
                    if (forward > 0.0) {
                        forward = 1.0;
                    }
                    else if (forward < 0.0) {
                        forward = -1.0;
                    }
                }
                entity.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f));
                entity.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f));
            }
        }
    }
    
    public static boolean rayTraceHitCheck(final Entity entity, final boolean shouldCheck) {
        return !shouldCheck || EntityUtil.mc.player.canEntityBeSeen(entity);
    }
    
    public static Color getColor(final Entity entity, final int red, final int green, final int blue, final int alpha, final boolean colorFriends) {
        Color color = new Color(red / 255.0f, green / 255.0f, blue / 255.0f, alpha / 255.0f);
        if (entity instanceof EntityPlayer && colorFriends && lover.friendManager.isFriend((EntityPlayer)entity)) {
            color = new Color(0.33333334f, 1.0f, 1.0f, alpha / 255.0f);
        }
        return color;
    }
    
    public static boolean isMoving() {
        return EntityUtil.mc.player.moveForward != 0.0 || EntityUtil.mc.player.moveStrafing != 0.0;
    }
    
    public static boolean isMoving(final EntityLivingBase entity) {
        return entity.moveForward != 0.0f || entity.moveStrafing != 0.0f;
    }
    
    public static EntityPlayer getClosestEnemy(final double distance) {
        EntityPlayer closest = null;
        for (final EntityPlayer player : EntityUtil.mc.world.playerEntities) {
            if (isntValid((Entity)player, distance)) {
                continue;
            }
            if (closest == null) {
                closest = player;
            }
            else {
                if (EntityUtil.mc.player.getDistanceSq((Entity)player) >= EntityUtil.mc.player.getDistanceSq((Entity)closest)) {
                    continue;
                }
                closest = player;
            }
        }
        return closest;
    }
    
    public static boolean checkCollide() {
        return !EntityUtil.mc.player.isSneaking() && (EntityUtil.mc.player.getRidingEntity() == null || EntityUtil.mc.player.getRidingEntity().fallDistance < 3.0f) && EntityUtil.mc.player.fallDistance < 3.0f;
    }
    
    public static BlockPos getPlayerPosWithEntity() {
        return new BlockPos((EntityUtil.mc.player.getRidingEntity() != null) ? EntityUtil.mc.player.getRidingEntity().posX : EntityUtil.mc.player.posX, (EntityUtil.mc.player.getRidingEntity() != null) ? EntityUtil.mc.player.getRidingEntity().posY : EntityUtil.mc.player.posY, (EntityUtil.mc.player.getRidingEntity() != null) ? EntityUtil.mc.player.getRidingEntity().posZ : EntityUtil.mc.player.posZ);
    }
    
    public static boolean isCrystalAtFeet(final EntityEnderCrystal crystal, final double range) {
        for (final EntityPlayer player : EntityUtil.mc.world.playerEntities) {
            if (EntityUtil.mc.player.getDistanceSq((Entity)player) > range * range) {
                continue;
            }
            if (lover.friendManager.isFriend(player)) {
                continue;
            }
            for (final Vec3d vec : EntityUtil.doubleLegOffsetList) {
                if (new BlockPos(player.getPositionVector()).add(vec.x, vec.y, vec.z) == crystal.getPosition()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static double[] forward(final double speed) {
        float forward = EntityUtil.mc.player.movementInput.moveForward;
        float side = EntityUtil.mc.player.movementInput.moveStrafe;
        float yaw = EntityUtil.mc.player.prevRotationYaw + (EntityUtil.mc.player.rotationYaw - EntityUtil.mc.player.prevRotationYaw) * EntityUtil.mc.getRenderPartialTicks();
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += ((forward > 0.0f) ? -45 : 45);
            }
            else if (side < 0.0f) {
                yaw += ((forward > 0.0f) ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            }
            else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        final double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        final double posX = forward * speed * cos + side * speed * sin;
        final double posZ = forward * speed * sin - side * speed * cos;
        return new double[] { posX, posZ };
    }
    
    public static void swingArmNoPacket(final EnumHand hand, final EntityLivingBase entity) {
        final ItemStack stack = entity.getHeldItem(hand);
        if (!stack.isEmpty() && stack.getItem().onEntitySwing(entity, stack)) {
            return;
        }
        if (!entity.isSwingInProgress || entity.swingProgressInt >= ((IEntityLivingBase)entity).getArmSwingAnimationEnd() / 2 || entity.swingProgressInt < 0) {
            entity.swingProgressInt = -1;
            entity.isSwingInProgress = true;
            entity.swingingHand = hand;
        }
    }
    
    public static Map<String, Integer> getTextRadarPlayers() {
        Map<String, Integer> output = new HashMap<String, Integer>();
        final DecimalFormat dfHealth = new DecimalFormat("#.#");
        dfHealth.setRoundingMode(RoundingMode.CEILING);
        final DecimalFormat dfDistance = new DecimalFormat("#.#");
        dfDistance.setRoundingMode(RoundingMode.CEILING);
        final StringBuilder healthSB = new StringBuilder();
        final StringBuilder distanceSB = new StringBuilder();
        for (final EntityPlayer player : EntityUtil.mc.world.playerEntities) {
            if (!player.isInvisible()) {
                if (player.getName().equals(EntityUtil.mc.player.getName())) {
                    continue;
                }
                final int hpRaw = (int)getHealth((Entity)player);
                final String hp = dfHealth.format(hpRaw);
                healthSB.append("\u00c2\u5594\u2467\u7afe");
                if (hpRaw >= 20) {
                    healthSB.append("a");
                }
                else if (hpRaw >= 10) {
                    healthSB.append("e");
                }
                else if (hpRaw >= 5) {
                    healthSB.append("6");
                }
                else {
                    healthSB.append("c");
                }
                healthSB.append(hp);
                final int distanceInt = (int)EntityUtil.mc.player.getDistance((Entity)player);
                final String distance = dfDistance.format(distanceInt);
                distanceSB.append("\u00c2\u5594\u2467\u7afe");
                if (distanceInt >= 25) {
                    distanceSB.append("a");
                }
                else if (distanceInt > 10) {
                    distanceSB.append("6");
                }
                else {
                    distanceSB.append("c");
                }
                distanceSB.append(distance);
                output.put(healthSB.toString() + " " + (lover.friendManager.isFriend(player) ? ChatFormatting.AQUA : ChatFormatting.RED) + player.getName() + " " + distanceSB.toString() + " \u00c2\u5594\u2467\u7afef0", (int)EntityUtil.mc.player.getDistance((Entity)player));
                healthSB.setLength(0);
                distanceSB.setLength(0);
            }
        }
        if (!output.isEmpty()) {
            output = MathUtil.sortByValue(output, false);
        }
        return output;
    }
    
    public static boolean isAboveBlock(final Entity entity, final BlockPos blockPos) {
        return entity.posY >= blockPos.getY();
    }
    
    public static void attackEntity(final Entity entity, final boolean packet) {
        if (packet) {
            EntityUtil.mc.player.connection.sendPacket((Packet)new CPacketUseEntity(entity));
        }
        else {
            EntityUtil.mc.playerController.attackEntity((EntityPlayer)EntityUtil.mc.player, entity);
        }
    }
    
    static {
        antiDropOffsetList = new Vec3d[] { new Vec3d(0.0, -2.0, 0.0) };
        platformOffsetList = new Vec3d[] { new Vec3d(0.0, -1.0, 0.0), new Vec3d(0.0, -1.0, -1.0), new Vec3d(0.0, -1.0, 1.0), new Vec3d(-1.0, -1.0, 0.0), new Vec3d(1.0, -1.0, 0.0) };
        legOffsetList = new Vec3d[] { new Vec3d(-1.0, 0.0, 0.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(0.0, 0.0, 1.0) };
        OffsetList = new Vec3d[] { new Vec3d(1.0, 1.0, 0.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(0.0, 2.0, 0.0) };
        antiStepOffsetList = new Vec3d[] { new Vec3d(-1.0, 2.0, 0.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(0.0, 2.0, -1.0) };
        antiScaffoldOffsetList = new Vec3d[] { new Vec3d(0.0, 3.0, 0.0) };
        doubleLegOffsetList = new Vec3d[] { new Vec3d(-1.0, 0.0, 0.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-2.0, 0.0, 0.0), new Vec3d(2.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -2.0), new Vec3d(0.0, 0.0, 2.0) };
    }
}
