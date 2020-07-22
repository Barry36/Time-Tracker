package com.cs446.group18.timetracker.constants;

public class QuadTreeConstant {
    public static final double QUADTREE_LAST_NODE_SIZE_IN_KM = 0.1;

    public static final double QUADTREE_LAST_NODE_SIZE_IN_DEGREE = distanceToDegree(QUADTREE_LAST_NODE_SIZE_IN_KM);

    public static final float ONE_DEGREE_IN_KM = 111.f;

    public static final int NORMALIZE_X = 180;

    public static final int NORMALIZE_Y = 90;

    public static double distanceToDegree(double distance) {
        return distance / ONE_DEGREE_IN_KM;
    }
}
