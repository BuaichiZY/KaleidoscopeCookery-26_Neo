package com.github.ysbbbbbb.kaleidoscopecookery.datagen.model;

import java.util.stream.IntStream;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class BlockModelGenerator extends BlockModelProvider {
   public BlockModelGenerator(PackOutput output, ExistingFileHelper existingFileHelper) {
      super(output, "kaleidoscope_cookery", existingFileHelper);
   }

   protected void registerModels() {
      Identifier stoveSide = this.modLoc("block/stove_side");
      Identifier stoveTop = this.modLoc("block/stove_top");
      Identifier stoveTopLit = this.modLoc("block/stove_top_lit");
      ((BlockModelBuilder)this.cube("stove", stoveTop, stoveTop, this.modLoc("block/stove_front"), stoveSide, stoveSide, stoveSide))
         .texture("particle", stoveSide);
      ((BlockModelBuilder)this.cube("stove_lit", stoveTopLit, stoveTopLit, this.modLoc("block/stove_front_lit"), stoveSide, stoveSide, stoveSide))
         .texture("particle", stoveSide);
      ((BlockModelBuilder)this.cross("block/chili_ristra/head", this.modLoc("block/chili_ristra/head"))).renderType("cutout");
      ((BlockModelBuilder)this.cross("block/chili_ristra/body", this.modLoc("block/chili_ristra/body"))).renderType("cutout");
      ((BlockModelBuilder)this.cross("block/chili_ristra/head_sheared", this.modLoc("block/chili_ristra/head_sheared"))).renderType("cutout");
      ((BlockModelBuilder)this.cross("block/chili_ristra/body_sheared", this.modLoc("block/chili_ristra/body_sheared"))).renderType("cutout");
      ((BlockModelBuilder)this.cross("block/strung_mushrooms/head", this.modLoc("block/strung_mushrooms/head"))).renderType("cutout");
      ((BlockModelBuilder)this.cross("block/strung_mushrooms/body", this.modLoc("block/strung_mushrooms/body"))).renderType("cutout");
      ((BlockModelBuilder)this.cross("block/strung_mushrooms/head_sheared", this.modLoc("block/strung_mushrooms/head_sheared"))).renderType("cutout");
      ((BlockModelBuilder)this.cross("block/strung_mushrooms/body_sheared", this.modLoc("block/strung_mushrooms/body_sheared"))).renderType("cutout");
      this.cubeColumn("straw_block", this.modLoc("block/straw_block_side"), this.modLoc("block/straw_block_end"));
      this.wood("oak");
      this.wood("spruce");
      this.wood("acacia");
      this.wood("bamboo");
      this.wood("birch");
      this.wood("cherry");
      this.wood("crimson");
      this.wood("dark_oak");
      this.wood("jungle");
      this.wood("mangrove");
      this.wood("warped");
      this.crop("tomato", 8);
      this.crop("chili", 8);
      this.crop("lettuce", 8);
      this.riceCrop();
      this.carpet();
   }

   public void wood(String name) {
      String cookStool = "block/cook_stool/" + name;
      ((BlockModelBuilder)this.withExistingParent(cookStool, this.modLoc("block/cook_stool/cook_stool"))).texture("particle", this.modLoc(cookStool));
      String chair = "block/chair/" + name;
      ((BlockModelBuilder)this.withExistingParent(chair, this.modLoc("block/chair/chair"))).texture("particle", this.modLoc(chair));
      String tableSingle = "block/table/" + name + "_single";
      Identifier tableTexture = this.modLoc("block/table/" + name);
      ((BlockModelBuilder)this.withExistingParent(tableSingle, this.modLoc("block/table/single"))).texture("particle", tableTexture);
      String tableLeft = "block/table/" + name + "_left";
      ((BlockModelBuilder)this.withExistingParent(tableLeft, this.modLoc("block/table/left"))).texture("particle", tableTexture);
      String tableRight = "block/table/" + name + "_right";
      ((BlockModelBuilder)this.withExistingParent(tableRight, this.modLoc("block/table/right"))).texture("particle", tableTexture);
      String tableMiddle = "block/table/" + name + "_middle";
      ((BlockModelBuilder)this.withExistingParent(tableMiddle, this.modLoc("block/table/middle"))).texture("particle", tableTexture);
      String tableLeftRot = "block/table/" + name + "_left_rot";
      ((BlockModelBuilder)this.withExistingParent(tableLeftRot, this.modLoc("block/table/left_rot"))).texture("particle", tableTexture);
      String tableRightRot = "block/table/" + name + "_right_rot";
      ((BlockModelBuilder)this.withExistingParent(tableRightRot, this.modLoc("block/table/right_rot"))).texture("particle", tableTexture);
      String tableMiddleRot = "block/table/" + name + "_middle_rot";
      ((BlockModelBuilder)this.withExistingParent(tableMiddleRot, this.modLoc("block/table/middle_rot"))).texture("particle", tableTexture);
   }

   public void carpet() {
      for (DyeColor color : DyeColor.values()) {
         String name = color.getName();
         String chairCarpet = "block/carpet/chair/" + name;
         ((BlockModelBuilder)this.withExistingParent(chairCarpet, this.modLoc("block/carpet/chair/chair_carpet")))
            .texture("particle", this.modLoc(chairCarpet));
         Identifier tableCarpetTexture = this.modLoc("block/carpet/table/" + name);
         String tableCarpetSingle = "block/carpet/table/" + name + "_single";
         ((BlockModelBuilder)this.withExistingParent(tableCarpetSingle, this.modLoc("block/carpet/table/single"))).texture("particle", tableCarpetTexture);
         String tableCarpetLeft = "block/carpet/table/" + name + "_left";
         ((BlockModelBuilder)this.withExistingParent(tableCarpetLeft, this.modLoc("block/carpet/table/left"))).texture("particle", tableCarpetTexture);
         String tableCarpetRight = "block/carpet/table/" + name + "_right";
         ((BlockModelBuilder)this.withExistingParent(tableCarpetRight, this.modLoc("block/carpet/table/right"))).texture("particle", tableCarpetTexture);
         String tableCarpetMiddle = "block/carpet/table/" + name + "_middle";
         ((BlockModelBuilder)this.withExistingParent(tableCarpetMiddle, this.modLoc("block/carpet/table/middle"))).texture("particle", tableCarpetTexture);
      }
   }

   public void crop(String name, int stage) {
      IntStream.range(0, stage).forEach(i -> {
         String id = "block/crop/%s/stage%d".formatted(name, i);
         ((BlockModelBuilder)this.singleTexture(id, this.modLoc("block/cross"), "cross", this.modLoc(id))).renderType("cutout");
      });
   }

   public void riceCrop() {
      IntStream.range(0, 8).forEach(i -> {
         String down = "block/crop/rice/stage%d_down".formatted(i);
         String middle = "block/crop/rice/stage%d_middle".formatted(i);
         String up = "block/crop/rice/stage%d_up".formatted(i);
         ((BlockModelBuilder)this.cross(down, this.modLoc(down))).renderType("cutout");
         ((BlockModelBuilder)this.cross(middle, this.modLoc(middle))).renderType("cutout");
         ((BlockModelBuilder)this.cross(up, this.modLoc(up))).renderType("cutout");
      });
   }
}
