/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.prz.stud.swawro.server.ServerAccessLayer.Packets.c2s;

/**
 *
 * @author Sebastian
 */
public class ChangePasswordRequest {
    private int packetId = 112;
    private String uuid;
    private String oldPassword;
    private String newPassword;
    private boolean logOutOtherDevices;

    public int getPacketId() {
        return packetId;
    }

    private void setPacketId(int packetId) {
        this.packetId = packetId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public boolean isLogOutOtherDevices() {
        return logOutOtherDevices;
    }

    public void setLogOutOtherDevices(boolean logOutOtherDevices) {
        this.logOutOtherDevices = logOutOtherDevices;
    }
}
