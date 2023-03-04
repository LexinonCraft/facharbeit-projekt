package com.lexinon.facharbeit;

import org.joml.Vector3i;

/**
 * An interface for octree nodes that can either be contained in the {@link Octree} class as its root node or as one of
 * an {@link InnerOctreeNode} child nodes.
 */
public interface IOctreeNode {

    /**
     * Render all voxels in this node's volume
     * @param originX          The x-component of the absolute origin of the volume
     * @param originY          The y-component of the absolute origin of the volume
     * @param originZ          The z-component of the absolute origin of the volume
     * @param volumeEdgeLength The volume's edge length
     * @param octree           The {@link Octree} object, which is used to obtain information necessary for the rendering
     */
    void render(int originX, int originY, int originZ, int volumeEdgeLength, Octree octree);

    /**
     * Set an empty voxel to a specific material
     * @param pos            The relative and bit-shifted position with (0,0,0) being the node's volume's center
     * @param material       The material of the voxel to be placed
     * @param remainingDepth The remaining depth till no more child nodes are created when adding a voxel in an empty volume
     * @param parentNode     This node's parent node
     * @param octree         The {@link Octree} object, which contains the queue for updating the meshes
     * @return The node to replace the current node with ({@code this} when it shouldn't be replaced)
     */
    IOctreeNode addVoxel(Vector3i pos, short material, int remainingDepth, IOctreeParentNode parentNode, Octree octree);

    /**
     * Set a voxel's material to empty
     * @param pos        The relative and bit-shifted position with (0,0,0) being the node's volume's center
     * @param parentNode This node's parent node
     * @param octree     The {@link Octree} object, which contains the queue for updating the meshes
     * @return The node to replace the current node with ({@code this} when it shouldn't be replaced)
     */
    IOctreeNode removeVoxel(Vector3i pos, IOctreeParentNode parentNode, Octree octree);

    /**
     * Delete this and all child nodes as well as the voxel array's meshes
     */
    void deleteEverything();

    void print(int depth);

}
