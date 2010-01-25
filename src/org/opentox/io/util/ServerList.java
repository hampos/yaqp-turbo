package org.opentox.io.util;

/**
 *
 * @author chung
 */
public enum ServerList {

    ambit("http://ambit.uni-plovdiv.bg:8080/ambit2", true, true, true),
    tum("http://opentox.informatik.tu-muenchen.de:8080/OpenTox", false, true, false),
    insilico("http://webservices.in-silico.ch", true, true, true);

    private final String uri;
    private final boolean supportsDataset, supportsFeatures, supportsCompounds;

    private ServerList(final String uri, final boolean sd, final boolean sf, final boolean sc){
        this.uri = uri;
        this.supportsCompounds = sc;
        this.supportsDataset = sd;
        this.supportsFeatures = sf;
    }

    public String getBaseURI(){
        return uri;
    }

    public boolean suppDataset(){
        return this.supportsDataset;
    }

    public boolean suppFeatures(){
        return this.supportsFeatures;
    }

    public boolean suppCompounds() {
        return this.supportsCompounds;
    }


}
