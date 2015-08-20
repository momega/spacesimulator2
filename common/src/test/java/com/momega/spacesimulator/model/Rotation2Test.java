package com.momega.spacesimulator.model;

import com.momega.spacesimulator.model.KeplerianOrbit;
import junit.framework.Assert;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.Test;

/**
 * Created by martin on 10/17/14.
 */
public class Rotation2Test {

    @Test
    public void zxzTest() {
        KeplerianOrbit keplerianOrbit = new KeplerianOrbit();
        keplerianOrbit.setSemimajorAxis(7406832.895913694);
        keplerianOrbit.setEccentricity(0.11085928349395398);
        keplerianOrbit.setArgumentOfPeriapsis(3.632666857853031);
        keplerianOrbit.setInclination(0.40818100468528823);
        keplerianOrbit.setAscendingNode(3.138751509359614);

        Vector3D v = new Vector3D(0.9907027401573953, -0.1253069535327467, 0.05297403176057446);
        //Vector3D tv = VectorUtils.transform(keplerianOrbit, v);
        Vector3D tv = v;
        tv = Orientation.rotateAboutAxis(tv, -keplerianOrbit.getAscendingNode(), new Vector3D(0,0,1));
        tv = Orientation.rotateAboutAxis(tv, -keplerianOrbit.getInclination(), new Vector3D(1,0,0));
        tv = Orientation.rotateAboutAxis(tv, -keplerianOrbit.getArgumentOfPeriapsis(), new Vector3D(0,0,1));

        Assert.assertEquals(0, tv.getZ(), 0.0001);
        Assert.assertEquals(-0.5850400486216911, tv.getY(), 0.0001);
        Assert.assertEquals(0.8110044028910874, tv.getX(), 0.0001);
    }
}
