package pe.elements;

/**
 * @author 尹硕
 */

public class LabelTuple implements Comparable<LabelTuple> {
    private int streamingLabel;
    private int stationaryLabel;
    private int drainID = -1;

    public LabelTuple(int stationaryLabel, int streamingLabel) {
        this.stationaryLabel = stationaryLabel;
        this.streamingLabel = streamingLabel;
    }

    public int getDrainID() {
        return drainID;
    }

    public void setDrainID(int drainID) {
        this.drainID = drainID;
    }

    public int getStreamingLabel() {
        return streamingLabel;
    }

    public int getStationaryLabel() {
        return stationaryLabel;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        LabelTuple o = (LabelTuple) obj;
        if (o.getStationaryLabel() != stationaryLabel) {
            return false;
        }
        return o.getStreamingLabel() == streamingLabel;
    }

    @Override
    public int hashCode() {
        return this.stationaryLabel * 1000 + this.streamingLabel;
    }

    @Override
    public int compareTo(LabelTuple o) {
        if ((o.getStationaryLabel() + o.getStreamingLabel()) < (this.getStreamingLabel() + this.getStationaryLabel())) {
            return -1;
        } else if ((o.getStationaryLabel() + o.getStreamingLabel()) > (this.getStreamingLabel() + this.getStationaryLabel())) {
            return 1;
        } else {
            if (o.getStationaryLabel() < this.getStationaryLabel()) {
                return -1;
            } else if (o.getStationaryLabel() > this.getStationaryLabel()) {
                return 1;
            }

        }
        return 0;
    }

    public boolean isValid() {
        if (streamingLabel == -1) {
            return false;
        }
        return stationaryLabel != -1;
    }

    @Override
    public String toString() {
        return "(" + stationaryLabel + "," + streamingLabel + ")";
    }


}
