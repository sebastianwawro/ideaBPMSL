package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.s2c;

import java.util.List;

import pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.to.RelationTO;

public class SyncRelationsResponse {
    private int statusCode;
    private List<RelationTO> relationTOList;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public List<RelationTO> getRelationTOList() {
        return relationTOList;
    }

    public void setRelationTOList(List<RelationTO> relationTOList) {
        this.relationTOList = relationTOList;
    }
}
