package com.procreate.geometry.manager;

import com.google.common.geometry.*;
import com.procreate.geometry.data.LatLangVO;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ProS2GeometryManager {
    private static final Logger log = Logger.getLogger(ProS2GeometryManager.class.getCanonicalName());

    private static ProS2GeometryManager manager;
    private ProS2GeometryManager(){}
    public static ProS2GeometryManager getManager(){
        if(manager == null){
            manager = new ProS2GeometryManager();
        }
        return manager;
    }

    public List<LatLangVO> getNearestLatLangs(int minLevel , int maxLevel, int modeLevel, LatLangVO vo, List<LatLangVO> listPoints){

        List<LatLangVO> listInsidePoints = new ArrayList<LatLangVO>();
        S2LatLng s2LatLng = S2LatLng.fromDegrees(vo.getLatitude(), vo.getLongitude());
        S2Cell cell = new S2Cell(s2LatLng);
        S2LatLngRect cellBound = cell.getRectBound();
        S2RegionCoverer coverer = new S2RegionCoverer();
        if(modeLevel>3 || modeLevel<1)
            log.warning("Geometry: mode level allowed range 1-3");
        else
            coverer.setLevelMod(modeLevel);
        if(minLevel>30 || minLevel<1)
            log.warning("Geometry: min allowed coverage level 1-30");
        else
            coverer.setMinLevel(minLevel);
        if(maxLevel>30 || maxLevel<1)
            log.warning("Geometry: max allowed coverage level 1-30");
        else
            coverer.setMaxLevel(maxLevel);
        S2CellUnion covering = coverer.getCovering(cellBound);
        for(LatLangVO data : listPoints){
            S2Point point = S2LatLng.fromDegrees(data.getLatitude(), data.getLongitude()).toPoint();
            if(covering.contains(point))
                listInsidePoints.add(data);
        }

        return listInsidePoints;
    }

    public List<LatLangVO> getNearestLatLangs(int maxLevel, LatLangVO vo, List<LatLangVO> listPoints){
        List<LatLangVO> listInsidePoints = new ArrayList<LatLangVO>();
        S2LatLng s2LatLng = S2LatLng.fromDegrees(vo.getLatitude(), vo.getLongitude());
        S2Cell cell = new S2Cell(s2LatLng);
        //S2Cell cell = new S2Cell(S2CellId.fromLatLng(s2LatLng).next().next().next().next());
        S2LatLngRect cellBound = cell.getRectBound();
        S2RegionCoverer coverer = new S2RegionCoverer();
        coverer.setLevelMod(1);
        if(maxLevel>30 || maxLevel<1)
            log.warning("Geometry: max allowed coverage level 1-30");
        else
            coverer.setMaxLevel(maxLevel);
        S2CellUnion covering = coverer.getCovering(cellBound);
        for(LatLangVO data : listPoints){
            S2Point point = S2LatLng.fromDegrees(data.getLatitude(), data.getLongitude()).toPoint();
            if(covering.contains(point))
                listInsidePoints.add(data);
        }
        return listInsidePoints;
    }

    public static void main(String[] args) {
        LatLangVO ameerpetMetro = new LatLangVO(17.435840, 78.444577, "Ameerpet Metro");
        int maxLevel = 6;
        for(LatLangVO vo: ProS2GeometryManager.getManager().getNearestLatLangs( maxLevel, ameerpetMetro, getSampleList())){
            System.out.println(vo.getName());
        }
    }

    private static List<LatLangVO> getSampleList(){
        LatLangVO mallepally = new LatLangVO(16.726989, 78.968163, "mallepally");
        LatLangVO chinthapalli = new LatLangVO(16.816011, 78.832657, "chinthapalli");
        LatLangVO YACHARAM = new LatLangVO(17.045755, 78.662801, "YACHARAM");
        LatLangVO IBRAHIMPATNAM = new LatLangVO(17.190126, 78.647607, "IBRAHIMPATNAM");
        LatLangVO DILSUKHNAGAR = new LatLangVO(17.368112, 78.527211, "DILSUKHNAGAR");
        LatLangVO ABIDS = new LatLangVO(17.391966, 78.476215, "ABIDS");
        LatLangVO KHAIRTHABAD = new LatLangVO(17.411602, 78.460816, "KHAIRTHABAD");
        LatLangVO PUNJAGUTTA = new LatLangVO(17.428607, 78.451162, "PUNJAGUTTA");
        LatLangVO AMEPUN16 = new LatLangVO(17.431793, 78.447712, "AMEPUN16");

        LatLangVO ameerpetMetro = new LatLangVO(17.435840, 78.444577, "Ameerpet Metro");

        LatLangVO SRNP18M = new LatLangVO(17.436998, 78.443818, "SRNP18M");
        LatLangVO SRNP6 = new LatLangVO(17.439806, 78.442617, "SRNP6");
        LatLangVO ESI19 = new LatLangVO(17.442301, 78.441281, "ESI19");
        LatLangVO ESI2 = new LatLangVO(17.446466, 78.438832, "ESI2");
        LatLangVO SRNAGAR_pochammaTemple = new LatLangVO(17.446999, 78.438651, "SR nagar Pocham Temple");
        LatLangVO ERG34 = new LatLangVO(17.448568, 78.437683, "ERG34");
        LatLangVO BTN20 = new LatLangVO(17.458424, 78.432957, "BTN20");
        LatLangVO BTN7 = new LatLangVO(17.461530, 78.431235, "BTN7");
        LatLangVO MSP27 = new LatLangVO(17.465250, 78.429468, "MSP27");
        LatLangVO MSP2 = new LatLangVO(17.471327, 78.426498, "MSP2");
        LatLangVO BLR17 = new LatLangVO(17.472949, 78.425388, "BLR17");
        LatLangVO BLR2 = new LatLangVO(17.476326, 78.422773, "BLR2");
        LatLangVO KUK40 = new LatLangVO(17.477994, 78.420198, "KUK40");
        LatLangVO KUK2 = new LatLangVO(17.484436, 78.412108, "KUK2");
        LatLangVO KPHB29 = new LatLangVO(17.489209, 78.408742, "KPHB29");
        LatLangVO KPHB2 = new LatLangVO(17.493382, 78.402637, "KPHB2");
        LatLangVO JNTU36 = new LatLangVO(17.495157, 78.398772, "JNTU36");
        LatLangVO JNTU3 = new LatLangVO(17.498467, 78.390205, "JNTU3");
        LatLangVO MYP46 = new LatLangVO(17.499360, 78.383588, "MYP46");
        LatLangVO MYP2 = new LatLangVO(17.496503, 78.373997, "MYP2");
        LatLangVO L1S1 = new LatLangVO(17.496477, 78.365736, "MYP46");
        LatLangVO alwynCross = new LatLangVO(17.494090, 78.351174, "Alwyn cross");
        LatLangVO Karachi_Chandanagar = new LatLangVO(17.494607, 78.327872, "Karachi Chandanagar");
        LatLangVO kaveriHotel = new LatLangVO(17.498644, 78.385182, "Kaveri hotel");
        LatLangVO greenfield = new LatLangVO(17.536732, 78.313298, "GreenFiled");

        LatLangVO NehruRingRoad = new LatLangVO(17.316744, 78.659956, "NehruRingRoad");
        LatLangVO LBNagar = new LatLangVO(17.346413, 78.550863, "LBNagar");
        LatLangVO Nalgonda = new LatLangVO(17.040617, 79.313811, "Nalgonda");
        LatLangVO purimetla = new LatLangVO(15.884484, 79.841403, "Purimetla");
        LatLangVO london = new LatLangVO(51.5001525, -0.1262355, "London");
        LatLangVO tirupathi = new LatLangVO(13.623012, 79.412957, "tirupathi");
        LatLangVO Vizag = new LatLangVO(17.706123, 83.229140, "Vizag");
        LatLangVO chennai = new LatLangVO(13.058276, 80.259684, "chennai");
        LatLangVO banglore = new LatLangVO(12.984261, 77.605720, "banglore");
        LatLangVO pune = new LatLangVO(18.508388, 73.812248, "pune");
        LatLangVO jammuAndKashmir = new LatLangVO(33.785567, 76.564509, "jammu and kashmir");




        List<LatLangVO> list = new ArrayList<>();
        list.add(tirupathi);list.add(Vizag);list.add(chennai);list.add(banglore);list.add(pune);
        list.add(mallepally);
        list.add(DILSUKHNAGAR);list.add(IBRAHIMPATNAM);list.add(YACHARAM);list.add(chinthapalli);
        list.add(AMEPUN16);list.add(PUNJAGUTTA);list.add(KHAIRTHABAD);list.add(ABIDS);
        list.add(ameerpetMetro);
        list.add(SRNP18M);list.add(SRNP6);
        list.add(ESI19);list.add(ESI2);
        list.add(SRNAGAR_pochammaTemple);
        list.add(ERG34);
        list.add(BTN20);list.add(BTN7);
        list.add(MSP27);list.add(MSP2);
        list.add(BLR17);list.add(BLR2);
        list.add(KUK40);list.add(KUK2);
        list.add(KPHB29);list.add(KPHB2);
        list.add(JNTU36);list.add(JNTU3);
        list.add(MYP46);list.add(MYP2);
        list.add(L1S1);
        list.add(alwynCross);
        list.add(Karachi_Chandanagar);
        list.add(kaveriHotel);
        list.add(greenfield);

        list.add(LBNagar);
        list.add(NehruRingRoad);
        list.add(Nalgonda);
        list.add(purimetla);
        list.add(london);

        return list;
    }


}
