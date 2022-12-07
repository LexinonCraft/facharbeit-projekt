package com.lexinonc.voxeleditor;

import java.util.Stack;

public class VoxelEditor implements AutoCloseable {

    private static VoxelEditor INSTANCE;

    private Stack<AutoCloseable> autoCloseables = new Stack<>();

    private VoxelEditor() {

    }

    public static VoxelEditor createOrGet() {
        if(INSTANCE != null)
            return INSTANCE;
        return INSTANCE = new VoxelEditor();
    }

    public VoxelEditor addAutoCloseable(AutoCloseable ac) {
        autoCloseables.push(ac);
        return this;
    }

    @Override
    public void close() throws Exception {
        while(!autoCloseables.empty()) {
            autoCloseables.pop().close();
        }
    }

}
