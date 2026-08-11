package com.github.ysbbbbbb.kaleidoscopecookery.util;

import com.google.common.collect.Lists;
import java.util.EnumMap;
import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Plane;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class VoxelShapeUtils {
   public static EnumMap<Direction, VoxelShape> horizontalShapes(@NotNull VoxelShape northShape) {
      EnumMap<Direction, VoxelShape> result = new EnumMap<>(Direction.class);

      for (Direction direction : Plane.HORIZONTAL) {
         result.put(direction, rotateShape(northShape, direction));
      }

      return result;
   }

   public static VoxelShape rotateShape(VoxelShape shape, Direction direction) {
      List<VoxelShape> parts = Lists.newArrayList();
      shape.forAllBoxes((x1, y1, z1, x2, y2, z2) -> {
         double rx1;
         double rz1;
         double rx2;
         double rz2;
         switch (direction) {
            case EAST:
               rx1 = 1.0 - z2;
               rz1 = x1;
               rx2 = 1.0 - z1;
               rz2 = x2;
               break;
            case SOUTH:
               rx1 = 1.0 - x2;
               rz1 = 1.0 - z2;
               rx2 = 1.0 - x1;
               rz2 = 1.0 - z1;
               break;
            case WEST:
               rx1 = z1;
               rz1 = 1.0 - x2;
               rx2 = z2;
               rz2 = 1.0 - x1;
               break;
            default:
               rx1 = x1;
               rz1 = z1;
               rx2 = x2;
               rz2 = z2;
         }

         parts.add(Shapes.box(rx1, y1, rz1, rx2, y2, rz2));
      });
      return parts.stream().reduce(Shapes.empty(), Shapes::or);
   }
}
