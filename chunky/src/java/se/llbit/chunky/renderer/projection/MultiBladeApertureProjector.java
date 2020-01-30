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

import java.util.Random;

import org.apache.commons.math3.util.FastMath;

import se.llbit.math.Vector3;

/**
* A projector for n-gon depth of field.
*/
public class MultiBladeApertureProjector extends ApertureProjector {
	
	protected final int bladeAmount;
	protected final double bladeRotation;
	protected final double lensRatio;
	
 public MultiBladeApertureProjector(Projector wrapped, double apertureSize,
     double subjectDistance, int bladeAmount, double bladeRotation, double lensRatio) {
   super(wrapped, apertureSize, subjectDistance);
   this.bladeAmount = bladeAmount;
   this.bladeRotation = bladeRotation;
   this.lensRatio = lensRatio;
 }

 @Override public void apply(double x, double y, Random random, Vector3 o, Vector3 d) {
   wrapped.apply(x, y, random, o, d);

   d.scale(subjectDistance);

   //sample on a square
   double rx = random.nextDouble(), ry = random.nextDouble();
   
   //folds square into a triangle
   if(rx-ry > 0.0) {
	   rx = rx-1.0;
	   ry = 1.0-ry;
   }
   
   //scale the triangle before rotation
   double side = FastMath.sin(FastMath.PI / bladeAmount);
   rx *= side;
   ry *= FastMath.sqrt(1.0-side*side);
   
   //rotation (placing the triangle on a random blade and adding the absolute blade rotation)
   int blade = (int) (random.nextDouble() * bladeAmount);
   double angle = bladeRotation + blade * 2.0 * FastMath.PI / bladeAmount;
   
   double oldRx = rx;
   rx = rx * FastMath.cos(angle) +    ry * FastMath.sin(angle);
   ry = ry * FastMath.cos(angle) - oldRx * FastMath.sin(angle);
   
   //scaling the aperture to give it that anamorphic effect
   rx *= aperture * Math.sqrt(1.0/this.lensRatio);
   ry *= aperture * Math.sqrt(    this.lensRatio);
   
   d.sub(rx, ry, 0);
   o.add(rx, ry, 0);
   
   d.scale(1.0/d.length());
 }
}