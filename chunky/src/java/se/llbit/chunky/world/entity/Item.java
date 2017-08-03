/*
 * Copyright (c) 2017 Jesper Öqvist <jesper@llbit.se>
 *
 * This file is part of Chunky.
 *
 * Chunky is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Chunky is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with Chunky.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.llbit.chunky.world.entity;

import se.llbit.chunky.resources.MinecraftFinder;
import se.llbit.chunky.resources.Texture;
import se.llbit.chunky.world.Material;
import se.llbit.chunky.world.material.TextureMaterial;
import se.llbit.chunky.world.model.CubeModel;
import se.llbit.chunky.world.model.JsonModel;
import se.llbit.json.JsonObject;
import se.llbit.json.JsonValue;
import se.llbit.math.Quad;
import se.llbit.math.Transform;
import se.llbit.math.Vector3;
import se.llbit.math.primitive.Primitive;

import java.util.Collection;
import java.util.LinkedList;

public class Item extends Entity {

  private final String id;
  private final CubeModel cubeModel;

  public Item(String id, Vector3 position) {
    super(position);
    this.id = id;
    if (id.startsWith("minecraft:")) {
      String model = "item/" + id.substring("minecraft:".length());
      cubeModel = new CubeModel(MinecraftFinder.getMinecraftJar(),
          JsonModel.get(MinecraftFinder.getMinecraftJar(), model), 16);
    } else {
      cubeModel = new CubeModel();
    }
  }

  @Override public Collection<Primitive> primitives(Vector3 offset) {
    Collection<Primitive> primitives = new LinkedList<>();
    Transform transform = Transform.NONE
        .translate(position.x + offset.x, position.y + offset.y, position.z + offset.z);
    // TODO: cache/reuse texture materials!
    for (int i = 0; i < cubeModel.quads.length; ++i) {
      Quad quad = cubeModel.quads[i];
      Texture texture = cubeModel.textures[i];
      quad.addTriangles(primitives, new TextureMaterial(texture), transform);
    }
    return primitives;
  }

  @Override public JsonValue toJson() {
    JsonObject json = new JsonObject();
    json.add("kind", "item");
    json.add("position", position.toJson());
    json.add("id", id);
    return json;
  }

  /**
   * Deserialize entity from JSON.
   *
   * @return deserialized entity, or {@code null} if it was not a valid entity
   */
  public static Entity fromJson(JsonObject json) {
    Vector3 position = new Vector3();
    position.fromJson(json.get("position").object());
    String id = json.get("id").asString("");
    return new Item(id, position);
  }
}
