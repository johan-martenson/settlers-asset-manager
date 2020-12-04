package org.appland.settlers.assets;

public class LBMGameResource implements GameResource {
    private LBMFile lbmFile;

    public LBMGameResource(LBMFile lbmFile) {
        this.lbmFile = lbmFile;
    }

    @Override
    public GameResourceType getType() {
        return GameResourceType.LBM_RESOURCE;
    }

    public LBMFile getLbmFile() {
        return lbmFile;
    }
}
