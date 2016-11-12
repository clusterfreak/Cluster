package ClusterGraphix;

import ClusterCore.FuzzyCMeans;
import ClusterCore.Point2D;

/**
 * Functions for ClusterBots
 * 
 * @version 0.1.0 (04-10-2016)
 * @author Thomas Heym
 */
public class ClusterBotNet {
	private ClusterBot clusterNet[];

	public ClusterBotNet(ClusterBot net[]) {
		clusterNet = net;
	}

	public ClusterBot[] getClusterBots() {
		return clusterNet;
	}

	/**
	 * PointMik-Value of each point in clusterNet &gt; mik_value for the
	 * associated cluster and &lt; mik_value for all other clusters if minimum 1
	 * Point in every cluster
	 * 
	 * @param mik_value
	 *            mik
	 * @return true/false
	 */
	public boolean clusterQuality(double mik_value) {
		boolean quality = false;
		if (clusterNet != null) {
			quality = true;
			for (int i = 0; i < clusterNet.length; i++) {
				if (clusterNet[i].getPoints() == 0 && clusterNet[i].getPointsPixel() == 0)
					quality = false;
				else {
					for (int p = 0; p < clusterNet[i].getPointMik().length; p++) {
						for (int k = 0; k < clusterNet[i].getPointMik()[p].length; k++) {
							if (k == i) {
								if (clusterNet[i].getPointMik()[p][k] < mik_value)
									quality = false;
							} else {
								if (clusterNet[i].getPointMik()[p][k] > mik_value)
									quality = false;
							}
						}
					}
				}
			}
		}
		return quality;
	}

	/**
	 * Set vi to the center with a fcm
	 */
	public void clusterBotCenter() {
		if (clusterNet != null) {
			for (int i = 0; i < clusterNet.length; i++) {
				double object[][] = new double[clusterNet[i].getPoints()][2];
				double vi[][] = new double[1][2];
				for (int p = 0; p < clusterNet[i].getPoints();p++){
					object[p][0]=clusterNet[i].getPoint()[p].x;
					object[p][1]=clusterNet[i].getPoint()[p].y;
				}
				FuzzyCMeans fcm = new FuzzyCMeans(object, 1);
				vi = fcm.determineClusterCenters(true, false);
				Point2D center = new Point2D(0.0, 0.0);
				center.x = vi[0][0];
				center.y = vi[0][1];
				clusterNet[i].setCenter(center);
				clusterNet[i].setCenterPixel(center.toPointPixel(clusterNet[i].getPixelOffset()));
				clusterNet[i].setPointMik(fcm.getMik());
				clusterNet[i].setModified(true);
				fcm = null;
			}
		}
	}
}
