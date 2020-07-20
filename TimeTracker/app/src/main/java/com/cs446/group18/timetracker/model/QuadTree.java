package com.cs446.group18.timetracker.model;

import com.cs446.group18.timetracker.constants.QuadTreeConstant;

import java.util.HashSet;
import java.util.Set;

public class QuadTree {

    public static final int TOTAL_X_DEGREES = 360; // -180 to 180 - longitude
    public static final int TOTAL_Y_DEGREES = 180; // -90 to 90   - latitude

    private QuadTreeNode rootNode;

    public QuadTree() {
        rootNode = new QuadTreeNode(0, 0, TOTAL_Y_DEGREES, TOTAL_X_DEGREES);
    }

    public QuadTree(QuadTreeNode rootNode) {
        this.rootNode = rootNode;
    }

    public synchronized void addNeighbour(long id, double latitude, double longitude) {
        Neighbour neighbour = new Neighbour(id, normalizeLatitude(latitude),
                normalizeLongitude(longitude));
        rootNode.addNeighbour(neighbour, QuadTreeConstant.QUADTREE_LAST_NODE_SIZE_IN_DEGREE);
    }

    public void removeNeighbour(long id) {
        rootNode.removeNeighbour(id);
    }

    public Set<Neighbour> findNeighbours(double latitude, double longitude, double rangeInKm) {
        Set<Neighbour> neighbourSet = new HashSet<>();
        double rangeInDegrees = QuadTreeConstant.distanceToDegree(rangeInKm);
        Rectangle2D areaOfInterest = getRangeAsRectangle(normalizeLatitude(latitude), normalizeLongitude(longitude), rangeInDegrees);
        rootNode.findNeighboursWithinRectangle(neighbourSet, areaOfInterest);
        return neighbourSet;
    }

    public Set<Long> findNeighboursIds(double latitude, double longitude, double rangeInKm) {
        Set<Neighbour> neighbourSet = findNeighbours(latitude, longitude, rangeInKm);
        Set<Long> neighboursIds = new HashSet<>();

        for (Neighbour neighbour : neighbourSet)
            neighboursIds.add(neighbour.getId());

        return neighboursIds;
    }

    protected QuadTreeNode getRootNode() {
        return rootNode;
    }

    public double normalizeLatitude(double latitude) {
        return latitude + QuadTreeConstant.NORMALIZE_Y;
    }

    public double normalizeLongitude(double longitude) {
        return longitude + QuadTreeConstant.NORMALIZE_X;
    }

    private Rectangle2D getRangeAsRectangle(double latitude, double longitude, double range) {
        return new Rectangle2D(Math.max(longitude - range, 0),
                Math.max(latitude - range, 0),
                Math.min(range * 2, TOTAL_X_DEGREES),
                Math.min(range * 2, TOTAL_Y_DEGREES));
    }

}
