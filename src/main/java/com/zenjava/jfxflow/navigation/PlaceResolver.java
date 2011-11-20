package com.zenjava.jfxflow.navigation;


import com.zenjava.jfxflow.actvity.HasNode;

/**
 * Provides a mapping between a Place and a Node (via the HasNode interface). The exact strategy used for mapping the
 * Place to the Node is up to the implementing class.
 */
public interface PlaceResolver
{
    /**
     * Maps the specified place to a Node. Implementations should use a specific matching strategy to determine whether
     * the Place is a match or not and map the Place to a Node accordingly.
     *
     * @param place the Place to map to a node.
     * @return the node that the place maps to or null if this mapping does not support the specified place.
     */
    public HasNode resolvePlace(Place place);
}
