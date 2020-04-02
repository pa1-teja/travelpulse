package com.trimax.vts.interfaces;

import com.trimax.vts.helper.MapAreaWrapper;

public interface CircleManagerListener {
    /**
     * Called when a circle was placed on the map
     * @param draggableCircle created circle
     */
    void onCreateCircle(MapAreaWrapper draggableCircle);

    /**
     * Called when resizing gesture finishes (user lifts the finger)
     * @param draggableCircle resized circle
     */
    void onResizeCircleEnd(MapAreaWrapper draggableCircle);

    /**
     * Called when move gesture finishes (user lifts the finger)
     * @param draggableCircle move circle
     */
    void onMoveCircleEnd(MapAreaWrapper draggableCircle);

    /**
     * Called when move gesture starts (user long presses the position marker)
     * @param draggableCircle circle about to be moved
     */
    void onMoveCircleStart(MapAreaWrapper draggableCircle);

    /**
     * Called when resize gesture starts (user long presses the resizing marker)
     * @param draggableCircle circle about to be resized
     */
    void onResizeCircleStart(MapAreaWrapper draggableCircle);

    /**
     * Called when the circle reaches the min possible radius (meters), if it was initialized with a min radius value
     * This happens during resizing gesture
     * Reducing size is automatically blocked when reached this value - no extra action required for this
     *
     * @param draggableCircle circle
     */
    void onMinRadius(MapAreaWrapper draggableCircle);

    /**
     * Called when the circle reaches the max possible radius (meters), if it was initialized with a max radius value
     * This happens during resizing gesture
     * Increasing size is automatically blocked when reached this value - no extra action required for this
     *
     * @param draggableCircle circle
     */
    void onMaxRadius(MapAreaWrapper draggableCircle);
}
