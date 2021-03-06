/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package asia.sejong.freedrawing.model;

import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

public class FDWireBendpoint extends Point implements Bendpoint {

	private static final long serialVersionUID = 3620395553184051283L;
	
	private float weight = 0.5f;
	private Dimension d1, d2;

	private FDWireBendpoint(Point p) {
		super(p);
	}
	
	public static FDWireBendpoint newInstance(Point location) {
		return new FDWireBendpoint(location);
	}

	public Dimension getFirstRelativeDimension() {
		return d1;
	}

	public Point getLocation() {
		return null;
	}

	public Dimension getSecondRelativeDimension() {
		return d2;
	}

	public float getWeight() {
		return weight;
	}

	public void setRelativeDimensions(Dimension dim1, Dimension dim2) {
		d1 = dim1;
		d2 = dim2;
	}

	public void setWeight(float w) {
		weight = w;
	}
	
	public void applyDelta(Point delta) {
		this.x = x + delta.x;
		this.y = y + delta.y;
	}
	
	//============================================================
	// Clonable
	
	@Override
	public FDWireBendpoint clone() {
		try {
			return (FDWireBendpoint)super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
}
