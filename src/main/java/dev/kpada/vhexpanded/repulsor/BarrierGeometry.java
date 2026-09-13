package dev.kpada.vhexpanded.repulsor;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import java.util.List;

public final class BarrierGeometry {
    public static final double THICKNESS = .125;
    private BarrierGeometry() {}
    public static AABB bounds(Vec3 center, double radius) {
        return new AABB(center.x-radius, center.y-radius, center.z-radius, center.x+radius, center.y+radius, center.z+radius);
    }
    public static List<AABB> faces(AABB b) {
        double t = Math.min(THICKNESS, b.getXsize()/2);
        return List.of(new AABB(b.minX,b.minY,b.minZ,b.minX+t,b.maxY,b.maxZ),
                new AABB(b.maxX-t,b.minY,b.minZ,b.maxX,b.maxY,b.maxZ),
                new AABB(b.minX,b.minY,b.minZ,b.maxX,b.minY+t,b.maxZ),
                new AABB(b.minX,b.maxY-t,b.minZ,b.maxX,b.maxY,b.maxZ),
                new AABB(b.minX,b.minY,b.minZ,b.maxX,b.maxY,b.minZ+t),
                new AABB(b.minX,b.minY,b.maxZ-t,b.maxX,b.maxY,b.maxZ));
    }
    public static VoxelShape at(AABB bounds, BlockPos pos) {
        VoxelShape shape = Shapes.empty();
        AABB cell = new AABB(pos);
        for (AABB face : faces(bounds)) if (face.intersects(cell)) shape = Shapes.or(shape,
                Shapes.create(face.intersect(cell).move(-pos.getX(), -pos.getY(), -pos.getZ())));
        return shape;
    }
    /** Horizontal radial displacement to clear the cube including the mob's width. */
    public static Vec3 displacement(Vec3 center, double radius, Vec3 target, double halfWidth) {
        double dx=target.x-center.x, dz=target.z-center.z;
        if (dx*dx+dz*dz < 1e-8) dx=1;
        double distance=Math.sqrt(dx*dx+dz*dz), ux=dx/distance, uz=dz/distance;
        double destination=(radius+halfWidth+.05)/Math.max(Math.abs(ux),Math.abs(uz));
        double travel=Math.max(0, destination-Math.sqrt(Math.pow(target.x-center.x,2)+Math.pow(target.z-center.z,2)));
        return new Vec3(ux*travel,0,uz*travel);
    }
}
