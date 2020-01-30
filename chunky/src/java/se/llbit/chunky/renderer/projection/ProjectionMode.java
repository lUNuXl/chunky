/* Copyright (c) 2014 Jesper Öqvist <jesper@llbit.se>
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
package se.llbit.chunky.renderer.projection;

/**
 * Available projection modes.
 */
public enum ProjectionMode {
  PINHOLE("Standard"),
  OCTA_ANAMORPHIC("Standard with octagonal anamorphic aperture"),
  HEXA("Standard with hexagonal aperture"),
  PENTA("Standard with pentagonal aperture"),
  DIAMOND("Standard with diamond aperture"),
  PARALLEL("Parallel"),
  FISHEYE("Fisheye"),
  STEREOGRAPHIC("Stereographic"),
  PANORAMIC("Panoramic (equirectangular)"),
  PANORAMIC_SLOT("Panoramic (slot)"),
  ODS_LEFT("Omni‐directional Stereo (left eye)"),
  ODS_RIGHT("Omni‐directional Stereo (right eye)");

  private final String niceName;

  ProjectionMode(String niceName) {
    this.niceName = niceName;
  }

  @Override public String toString() {
    return niceName;
  }

  public static ProjectionMode get(String name) {
    try {
      return ProjectionMode.valueOf(name);
    } catch (IllegalArgumentException e) {
      return PINHOLE;
    }
  }
}
