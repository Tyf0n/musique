/*
 * Copyright (C) 2010 in-somnia
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.jaad.mp4.boxes.impl;

import net.sourceforge.jaad.mp4.boxes.FullBox;
import net.sourceforge.jaad.mp4.MP4InputStream;
import java.io.IOException;
import java.util.Date;
import net.sourceforge.jaad.mp4.boxes.Utils;

/**
 * The movie header box defines overall information which is media-independent,
 * and relevant to the entire presentation considered as a whole.
 * @author in-somnia
 */
public class MovieHeaderBox extends FullBox {

	private Date creationTime, modificationTime;
	private long timeScale, duration;
	private double rate, volume;
	private double[] matrix;
	private long nextTrackID;

	public MovieHeaderBox() {
		super("Movie Header Box", "mvhd");
		matrix = new double[9];
	}

	@Override
	public void decode(MP4InputStream in) throws IOException {
		super.decode(in);
		if(version==1) {
			creationTime = Utils.getDate(in.readBytes(8));
			modificationTime = Utils.getDate(in.readBytes(8));
			timeScale = in.readBytes(4);
			duration = in.readBytes(8);
		}
		else {
			creationTime = Utils.getDate(in.readBytes(4));
			modificationTime = Utils.getDate(in.readBytes(4));
			timeScale = in.readBytes(4);
			duration = in.readBytes(4);
		}

		//rate: 16.16 fixed point
		rate = in.readFixedPoint(4, MP4InputStream.MASK16);
		//volume: 8.8 fixed point
		volume = in.readFixedPoint(2, MP4InputStream.MASK8);

		in.skipBytes(2); //reserved
		in.skipBytes(4); //reserved
		in.skipBytes(4); //reserved

		for(int i = 0; i<9; i++) {
			matrix[i] = in.readFixedPoint(4, MP4InputStream.MASK16);
		}

		in.skipBytes(24); //reserved

		nextTrackID = in.readBytes(4);

		left = 0;
	}

	/**
	 * The creation time is an integer that declares the creation time of the
	 * presentation in seconds since midnight, Jan. 1, 1904, in UTC time.
	 * @return the creation time
	 */
	public Date getCreationTime() {
		return creationTime;
	}

	/**
	 * The modification time is an integer that declares the most recent time
	 * the presentation was modified in seconds since midnight, Jan. 1, 1904,
	 * in UTC time.
	 */
	public Date getModificationTime() {
		return modificationTime;
	}

	/**
	 * The time-scale is an integer that specifies the time-scale for the entire
	 * presentation; this is the number of time units that pass in one second.
	 * For example, a time coordinate system that measures time in sixtieths of
	 * a second has a time scale of 60.
	 * @return the time-scale
	 */
	public long getTimeScale() {
		return timeScale;
	}

	/**
	 * The duration is an integer that declares length of the presentation (in
	 * the indicated timescale). This property is derived from the
	 * presentation's tracks: the value of this field corresponds to the
	 * duration of the longest track in the presentation. 
	 * @return the duration of the longest track
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * The rate is a floting point number that indicates the preferred rate
	 * to play the presentation; 1.0 is normal forward playback
	 * @return the playback rate
	 */
	public double getRate() {
		return rate;
	}

	/**
	 * The volume is a floating point number that indicates the preferred
	 * playback volume: 0.0 is mute, 1.0 is normal volume.
	 * @return the volume
	 */
	public double getVolume() {
		return volume;
	}

	/**
	 * Provides a transformation matrix for the video:
	 * [A,B,U,C,D,V,X,Y,W]
	 * A: width scale
	 * B: width rotate
	 * U: width angle
	 * C: height rotate
	 * D: height scale
	 * V: height angle
	 * X: position from left
	 * Y: position from top
	 * W: divider scale (restricted to 1.0)
	 *
	 * The normal values for scale are 1.0 and for rotate 0.0.
	 * The angles are restricted to 0.0.
	 *
	 * @return the transformation matrix for the video
	 */
	public double[] getTransformationMatrix() {
		return matrix;
	}

	/**
	 * The next-track-ID is a non-zero integer that indicates a value to use
	 * for the track ID of the next track to be added to this presentation. Zero
	 * is not a valid track ID value. The value shall be larger than the largest
	 * track-ID in use. If this value is equal to all 1s (32-bit), and a new
	 * media track is to be added, then a search must be made in the file for an
	 * unused track identifier.
	 * @return the ID for the next track
	 */
	public long getNextTrackID() {
		return nextTrackID;
	}
}
