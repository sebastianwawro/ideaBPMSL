/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.prz.stud.swawro.server.controller.helpers;

import java.util.ArrayList;
import java.util.List;
import pl.edu.prz.stud.swawro.server.Model.Abnormal;
import pl.edu.prz.stud.swawro.server.Model.BpMeasure;
import pl.edu.prz.stud.swawro.server.Model.EnumValue.AbnormalType;
import pl.edu.prz.stud.swawro.server.Model.EnumValue.BpHTType;
import pl.edu.prz.stud.swawro.server.Model.HrMeasure;
import pl.edu.prz.stud.swawro.server.Model.OxyMeasure;
import pl.edu.prz.stud.swawro.server.Model.Relation;
import pl.edu.prz.stud.swawro.server.Model.Settings;
import pl.edu.prz.stud.swawro.server.Model.SleepMeasure;

/**
 *
 * @author Sebastian
 */
public class AbnormalHelper {
    private static final AbnormalHelper _instance = new AbnormalHelper();
    private static final int BP_HIGH_TOO_HIGH_STAGE_1 = 140;
    private static final int BP_LOW_TOO_HIGH_STAGE_1 = 90;
    private static final int BP_HIGH_TOO_HIGH_STAGE_2 = 160;
    private static final int BP_LOW_TOO_HIGH_STAGE_2 = 100;
    private static final int BP_HIGH_TOO_HIGH_STAGE_3 = 180;
    private static final int BP_LOW_TOO_HIGH_STAGE_3 = 110;
    private static final int BP_HIGH_TOO_LOW = 100;
    private static final int BP_LOW_TOO_LOW = 60;
    private static final int HR_TOO_HIGH = 100;
    private static final int HR_TOO_LOW = 60;
    private static final int OXY_TOO_LOW = 94;
    private static final int DEEP_SLEEP_TIME_TOO_LOW = 5400;
    private static final int DEEP_SLEEP_PER_TOO_LOW = 20;
    
    private AbnormalHelper(){}
    
    public static final AbnormalHelper getInstance() {
        return _instance;
    }
    
    public List<Settings> getActiveSettings(List<Relation> relations, List<Settings> settings) {
        List<Settings> activeSettings = new ArrayList<>();
        for (Relation rel : relations) {
            for (Settings set : settings) {
                if (rel.getDoctor().getId() == set.getDoctor().getId() && rel.getUser().getId() == set.getUser().getId()) { //nieoptymalne
                    activeSettings.add(set);
                }
            }
        }
        return activeSettings;
    }
    
    private BpHTType bpIsTooHigh(BpMeasure bpm, BpHTType type) {
        switch (type) {
            case DISABLED:
                return BpHTType.DISABLED;
            case STAGE_1:
                if (bpm.getBpHigh() >= BP_HIGH_TOO_HIGH_STAGE_3 || bpm.getBpLow() >= BP_LOW_TOO_HIGH_STAGE_3)
                    return BpHTType.STAGE_3;
                if (bpm.getBpHigh() >= BP_HIGH_TOO_HIGH_STAGE_2 || bpm.getBpLow() >= BP_LOW_TOO_HIGH_STAGE_2)
                    return BpHTType.STAGE_2;
                if (bpm.getBpHigh() >= BP_HIGH_TOO_HIGH_STAGE_1 || bpm.getBpLow() >= BP_LOW_TOO_HIGH_STAGE_1)
                    return BpHTType.STAGE_1;
                break;
            case STAGE_2:
                if (bpm.getBpHigh() >= BP_HIGH_TOO_HIGH_STAGE_3 || bpm.getBpLow() >= BP_LOW_TOO_HIGH_STAGE_3)
                    return BpHTType.STAGE_3;
                if (bpm.getBpHigh() >= BP_HIGH_TOO_HIGH_STAGE_2 || bpm.getBpLow() >= BP_LOW_TOO_HIGH_STAGE_2)
                    return BpHTType.STAGE_2;
                break;
            case STAGE_3:
                if (bpm.getBpHigh() >= BP_HIGH_TOO_HIGH_STAGE_3 || bpm.getBpLow() >= BP_LOW_TOO_HIGH_STAGE_3)
                    return BpHTType.STAGE_3;
                break;
            default:
                break;
        }
        return BpHTType.DISABLED;
    }
    
    private boolean bpIsTooLow(BpMeasure bpm) {
        return bpm.getBpHigh() <= BP_HIGH_TOO_LOW || bpm.getBpLow() <= BP_LOW_TOO_LOW;
    }
    
    public List<Abnormal> prepareAbnormalListForBpMeasures(List<Relation> relations, List<Settings> settings, List<BpMeasure> bpMeasures) {
        List<Abnormal> abnormals = new ArrayList<>();
        List<Settings> activeSettings = getActiveSettings(relations, settings);
        if (activeSettings == null || activeSettings.isEmpty()) abnormals = null;
        else {
            for (Settings set : activeSettings) {
                for (BpMeasure bpm: bpMeasures) { //jeśli customowe mają wyższy priorytet to będą musiały być wyżej
                    if(set.getCustBpHighMax() != -1 && set.getCustBpLowMax() != -1 &&
                            (bpm.getBpHigh() > set.getCustBpHighMax() || bpm.getBpLow() > set.getCustBpLowMax())) {
                        Abnormal ab = new Abnormal();
                        ab.setAbType(AbnormalType.BP_CUSTOM_HIGH.ordinal());
                        ab.setBpHigh(bpm.getBpHigh());
                        ab.setBpLow(bpm.getBpLow());
                        ab.setDate(bpm.getDate());
                        ab.setUser(set.getUser());
                        ab.setDoctor(set.getDoctor());
                        abnormals.add(ab);
                        continue;
                    }
                    
                    if(set.getCustBpHighMin() != -1 && set.getCustBpLowMin() != -1 &&
                            (bpm.getBpHigh() < set.getCustBpHighMin() || bpm.getBpLow() < set.getCustBpLowMin())) {
                        Abnormal ab = new Abnormal();
                        ab.setAbType(AbnormalType.BP_CUSTOM_LOW.ordinal());
                        ab.setBpHigh(bpm.getBpHigh());
                        ab.setBpLow(bpm.getBpLow());
                        ab.setDate(bpm.getDate());
                        ab.setUser(set.getUser());
                        ab.setDoctor(set.getDoctor());
                        abnormals.add(ab);
                        continue;
                    }
                    
                    BpHTType bpHTType = null;
                    bpHTType = bpIsTooHigh(bpm, BpHTType.getValues()[set.getBpHighAb()]);
                    if (bpHTType.ordinal() > 0) {
                        Abnormal ab = new Abnormal();
                        ab.setAbType(AbnormalType.getValues()[bpHTType.ordinal()-1].ordinal()); //niezbyt poprawne ale wydajne
                        ab.setBpHigh(bpm.getBpHigh());
                        ab.setBpLow(bpm.getBpLow());
                        ab.setDate(bpm.getDate());
                        ab.setUser(set.getUser());
                        ab.setDoctor(set.getDoctor());
                        abnormals.add(ab);
                        continue;
                    }
                    
                    int bpLow = 0;
                    if (set.getBpLowAb() == 1 && bpIsTooLow(bpm)) {
                        Abnormal ab = new Abnormal();
                        ab.setAbType(AbnormalType.BP_LOW.ordinal()); //niezbyt poprawne ale wydajne
                        ab.setBpHigh(bpm.getBpHigh());
                        ab.setBpLow(bpm.getBpLow());
                        ab.setDate(bpm.getDate());
                        ab.setUser(set.getUser());
                        ab.setDoctor(set.getDoctor());
                        abnormals.add(ab);
                        continue;
                    }
                }
            }
        }
        return abnormals;
    }
    
    public List<Abnormal> prepareAbnormalListForHrMeasures(List<Relation> relations, List<Settings> settings, List<HrMeasure> hrMeasures) {
        List<Abnormal> abnormals = new ArrayList<>();
        List<Settings> activeSettings = getActiveSettings(relations, settings);
        if (activeSettings == null || activeSettings.isEmpty()) abnormals = null;
        else {
            for (Settings set : activeSettings) {
                for (HrMeasure hrm: hrMeasures) { //jeśli customowe mają wyższy priorytet to będą musiały być wyżej
                    if (set.getCustHrMax() != -1 && hrm.getHr() > set.getCustHrMax()) {
                        Abnormal ab = new Abnormal();
                        ab.setAbType(AbnormalType.HR_CUSTOM_HIGH.ordinal());
                        ab.setHr(hrm.getHr());
                        ab.setDate(hrm.getDate());
                        ab.setUser(set.getUser());
                        ab.setDoctor(set.getDoctor());
                        abnormals.add(ab);
                        continue;
                    }
                    
                    if (set.getCustHrMin() != -1 && hrm.getHr() < set.getCustHrMin()) {
                        Abnormal ab = new Abnormal();
                        ab.setAbType(AbnormalType.HR_CUSTOM_LOW.ordinal());
                        ab.setHr(hrm.getHr());
                        ab.setDate(hrm.getDate());
                        ab.setUser(set.getUser());
                        ab.setDoctor(set.getDoctor());
                        abnormals.add(ab);
                        continue;
                    }
                    
                    if (set.getHrHighAb() == 1 && hrm.getHr() > HR_TOO_HIGH) {
                        Abnormal ab = new Abnormal();
                        ab.setAbType(AbnormalType.HR_HIGH.ordinal());
                        ab.setHr(hrm.getHr());
                        ab.setDate(hrm.getDate());
                        ab.setUser(set.getUser());
                        ab.setDoctor(set.getDoctor());
                        abnormals.add(ab);
                        continue;
                    }
                    
                    if (set.getHrLowAb() == 1 && hrm.getHr() < HR_TOO_LOW) {
                        Abnormal ab = new Abnormal();
                        ab.setAbType(AbnormalType.HR_LOW.ordinal());
                        ab.setHr(hrm.getHr());
                        ab.setDate(hrm.getDate());
                        ab.setUser(set.getUser());
                        ab.setDoctor(set.getDoctor());
                        abnormals.add(ab);
                        continue;
                    }
                }
            }
        }
        return abnormals;
    }
    
    public List<Abnormal> prepareAbnormalListForOxyMeasures(List<Relation> relations, List<Settings> settings, List<OxyMeasure> oxyMeasures) {
        List<Abnormal> abnormals = new ArrayList<>();
        List<Settings> activeSettings = getActiveSettings(relations, settings);
        if (activeSettings == null || activeSettings.isEmpty()) abnormals = null;
        else {
            for (Settings set : activeSettings) {
                for (OxyMeasure om: oxyMeasures) { //jeśli customowe mają wyższy priorytet to będą musiały być wyżej
                    if (set.getCustOxyMin() != -1 && om.getOxy() < set.getCustOxyMin()) {
                        Abnormal ab = new Abnormal();
                        ab.setAbType(AbnormalType.OXY_CUSTOM_LOW.ordinal());
                        ab.setOxy(om.getOxy());
                        ab.setDate(om.getDate());
                        ab.setUser(om.getUser());
                        ab.setDoctor(set.getDoctor());
                        abnormals.add(ab);
                        continue;
                    }
                    
                    if(set.getOxyAb() == 1 && om.getOxy() < OXY_TOO_LOW) {
                        Abnormal ab = new Abnormal();
                        ab.setAbType(AbnormalType.OXY_LOW.ordinal());
                        ab.setOxy(om.getOxy());
                        ab.setDate(om.getDate());
                        ab.setUser(om.getUser());
                        ab.setDoctor(set.getDoctor());
                        abnormals.add(ab);
                        continue;
                    }
                }
            }
        }
        return abnormals;
    }
    
    public List<Abnormal> prepareAbnormalListForSleepMeasures(List<Relation> relations, List<Settings> settings, List<SleepMeasure> sleepMeasures) {
        List<Abnormal> abnormals = new ArrayList<>();
        List<Settings> activeSettings = getActiveSettings(relations, settings);
        if (activeSettings == null || activeSettings.isEmpty()) abnormals = null;
        else {
            for (Settings set : activeSettings) {
                for (SleepMeasure sm: sleepMeasures) { //jeśli customowe mają wyższy priorytet to będą musiały być wyżej
                    int deepSleepPer = (int)((long)sm.getDeep() * 100 / (long)sm.getTotal());
                    
                    if (set.getCustDeepSleepMinTime() != -1 && sm.getDeep() < set.getCustDeepSleepMinTime()) {
                        Abnormal ab = new Abnormal();
                        ab.setAbType(AbnormalType.DEEP_SLEEP_CUSTOM_LOW_TIME.ordinal());
                        ab.setDeepSleepTime(sm.getDeep());
                        ab.setDeepSleepPer(deepSleepPer);
                        ab.setDate(sm.getDate());
                        ab.setUser(sm.getUser());
                        ab.setDoctor(set.getDoctor());
                        abnormals.add(ab);
                        continue;
                    }
                    
                    if (set.getCustDeepSleepMinPer()!= -1 && sm.getDeep() < set.getCustDeepSleepMinPer()) {
                        Abnormal ab = new Abnormal();
                        ab.setAbType(AbnormalType.DEEP_SLEEP_CUSTOM_LOW_PER.ordinal());
                        ab.setDeepSleepTime(sm.getDeep());
                        ab.setDeepSleepPer(deepSleepPer);
                        ab.setDate(sm.getDate());
                        ab.setUser(sm.getUser());
                        ab.setDoctor(set.getDoctor());
                        abnormals.add(ab);
                        continue;
                    }
                    
                    if (set.getDeepSleepAb() == 1 && (sm.getDeep() < DEEP_SLEEP_TIME_TOO_LOW || deepSleepPer < DEEP_SLEEP_PER_TOO_LOW)) {
                        Abnormal ab = new Abnormal();
                        if (sm.getDeep() < DEEP_SLEEP_TIME_TOO_LOW)
                            ab.setAbType(AbnormalType.DEEP_SLEEP_LOW_TIME.ordinal());
                        else
                            ab.setAbType(AbnormalType.DEEP_SLEEP_LOW_PER.ordinal());
                        ab.setDeepSleepTime(sm.getDeep());
                        ab.setDeepSleepPer(deepSleepPer);
                        ab.setDate(sm.getDate());
                        ab.setUser(sm.getUser());
                        ab.setDoctor(set.getDoctor());
                        abnormals.add(ab);
                        continue;
                    }
                }
            }
        }
        return abnormals;
    }
    
}
