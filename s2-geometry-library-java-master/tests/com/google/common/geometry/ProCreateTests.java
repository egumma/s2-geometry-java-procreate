package com.google.common.geometry;

import com.procreate.geometry.data.LatLangVO;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ProCreateTests extends GeometryTestCase {

    private static Logger logger = Logger.getLogger(ProCreateTests.class.getName());

    public void testContainsIsWrappedCorrectly(){
        S2LatLngRect london = new S2LatLngRect(S2LatLng.fromDegrees(51.3368602, 0.4931979),
                S2LatLng.fromDegrees(51.7323965, 0.1495211));
        S2LatLngRect e14lj = new S2LatLngRect(S2LatLng.fromDegrees(51.5213527, -0.0476026),
                S2LatLng.fromDegrees(51.5213527, -0.0476026));
        assertTrue(london.contains(e14lj));
    }

    public void testS2CellIdEqualsIsWrappedCorrectly(){
        S2LatLng london = S2LatLng.fromDegrees(51.5001525, -0.1262355);
        //S2CellId cell = S2CellId.fromPoint(london.toPoint());
        S2CellId cell = S2CellId.fromLatLng(london);
        S2CellId same_cell = S2CellId.fromPoint(london.toPoint());
        assertEquals(cell, same_cell);
    }

    public void testS2CellIdComparsionIsWrappedCorrectly() {
        S2LatLng london = S2LatLng.fromDegrees(51.5001525, -0.1262355);
        S2CellId cell = S2CellId.fromPoint(london.toPoint());
        //logger.info("london cellId: "+cell);
        //logger.info("london next cellId: "+cell.next());
    //assertLess(cell, cell.next())
    //assertGreater(cell.next(), cell)
    }

    public void testCovererIsWrappedCorrectly(){
        S2LatLngRect london = new S2LatLngRect(S2LatLng.fromDegrees(51.3368602, 0.4931979),
                S2LatLng.fromDegrees(51.7323965, 0.1495211));
        S2LatLngRect e14lj = new S2LatLngRect(S2LatLng.fromDegrees(51.5213527, -0.0476026),
                S2LatLng.fromDegrees(51.5213527, -0.0476026));
        S2RegionCoverer coverer = new S2RegionCoverer();
        coverer.setMaxCells(6);
        assertEquals(6, coverer.maxCells());
        S2CellUnion covering = coverer.getCovering(e14lj);

        for(S2CellId cellid : covering.cellIds()){
            assertTrue(london.contains(new S2Cell(cellid)));
        }
        S2CellUnion interior = coverer.getInteriorCovering(e14lj);
        for (S2CellId cellid : interior.cellIds()) {
            assertTrue(london.contains(new S2Cell(cellid)));
        }
    }

    public void testS2PolygonIsWrappedCorrectly(){
        S2LatLng london =  S2LatLng.fromDegrees(51.5001525, -0.1262355);
        S2Cell cell = new S2Cell(london);
        S2LatLngRect cellBound = cell.getRectBound();
        S2Loop cellLoop = new S2Loop(cell, cellBound);
        S2Polygon polygon = new S2Polygon(cellLoop);
        assertEquals(polygon.numLoops(), 1);
        S2Point point = london.toPoint();
        assertTrue(polygon.contains(point));
    }

    public void testS2PolygonInitNestedIsWrappedCorrectly(){
        S2LatLng london =  S2LatLng.fromDegrees(51.5001525, -0.1262355);
        S2Cell cell = new S2Cell(london);
        S2LatLngRect cellBound = cell.getRectBound();
        S2Loop smallLoop = new S2Loop(cell, cellBound);
        S2Loop bigLoop = new S2Loop(new S2Cell(S2CellId.fromLatLng(london).parent(1)));
        S2Polygon polygon = new S2Polygon();
        List<S2Loop> loops = new ArrayList<S2Loop>();loops.add(smallLoop);loops.add(bigLoop);
        polygon.init(loops);
    }

    public void testS2LatLngRectRegion(){
        S2LatLngRect rect = new S2LatLngRect(S2LatLng.fromDegrees(1.0, 2.0),
                 S2LatLng.fromDegrees(3.0, 4.0));

        S2Point inside = S2LatLng.fromDegrees(2.0, 3.0).toPoint();
        S2Point outside = S2LatLng.fromDegrees(0.0, 0.0).toPoint();

        assertTrue(rect.contains(inside));
        assertFalse(rect.contains(outside));
        assertTrue(rect.contains(new S2Cell(inside)));
        assertFalse(rect.contains(new S2Cell(outside)));
        assertTrue(rect.mayIntersect(new S2Cell(inside)));
        assertFalse(rect.mayIntersect(new S2Cell(outside)));
    }


    public void testS2CellRegion() {
        S2LatLng s2LatLng =  S2LatLng.fromDegrees(3.0, 4.0);
        S2Cell cell = new S2Cell(S2CellId.fromLatLng(s2LatLng).parent(8));

        S2Point inside = S2LatLng.fromDegrees(3.0, 4.0).toPoint();
        S2Point outside = S2LatLng.fromDegrees(30.0, 40.0).toPoint();

        assertTrue(cell.contains(inside));
        assertFalse(cell.contains(outside));
        assertTrue(cell.contains(new S2Cell(inside)));
        assertFalse(cell.contains(new S2Cell(outside)));
        assertTrue(cell.mayIntersect(new S2Cell(inside)));
        assertFalse(cell.mayIntersect(new S2Cell(outside)));

        S2Cap capBound = cell.getCapBound();
        assertTrue(capBound.contains(inside));
        assertFalse(capBound.contains(outside));

        S2LatLngRect rectBound = cell.getRectBound();
        assertTrue(rectBound.contains(inside));
        assertFalse(rectBound.contains(outside));
    }

    public void testS2CellUnionRegion(){
        S2LatLng s2LatLng =  S2LatLng.fromDegrees(3.0, 4.0);
        S2CellId cellId = S2CellId.fromLatLng(s2LatLng).parent(8);
        ArrayList<S2CellId> cellIds = new ArrayList<S2CellId>();cellIds.add(cellId);
        S2CellUnion cellUnion = new S2CellUnion();
        cellUnion.initRawCellIds(cellIds);

        S2Point inside = S2LatLng.fromDegrees(3.0, 4.0).toPoint();
        S2Point outside = S2LatLng.fromDegrees(30.0, 40.0).toPoint();

        assertTrue(cellUnion.contains(inside));
        assertFalse(cellUnion.contains(outside));
        assertTrue(cellUnion.contains(new S2Cell(inside)));
        assertFalse(cellUnion.contains(new S2Cell(outside)));
        assertTrue(cellUnion.mayIntersect(new S2Cell(inside)));
        assertFalse(cellUnion.mayIntersect(new S2Cell(outside)));

        S2Cap capBound = cellUnion.getCapBound();
        assertTrue(capBound.contains(inside));
        assertFalse(capBound.contains(outside));

        S2LatLngRect rectBound = cellUnion.getRectBound();
        assertTrue(rectBound.contains(inside));
        assertFalse(rectBound.contains(outside));
    }

    public void testLive(){
        LatLangVO greenfield = new LatLangVO(17.536732, 78.313298, "GreenFiled");
        LatLangVO london = new LatLangVO(51.5001525, -0.1262355, "London");
        LatLangVO kaveriHotel = new LatLangVO(17.498644, 78.385182, "Kaveri hotel");



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


        LatLangVO NehruRingRoad = new LatLangVO(17.316744, 78.659956, "NehruRingRoad");
        LatLangVO LBNagar = new LatLangVO(17.346413, 78.550863, "LBNagar");
        LatLangVO Nalgonda = new LatLangVO(17.040617, 79.313811, "Nalgonda");
        LatLangVO purimetla = new LatLangVO(15.884484, 79.841403, "Purimetla");


        List<LatLangVO> listAMPT_KAVERI = new ArrayList<>();
        listAMPT_KAVERI.add(ameerpetMetro);
        listAMPT_KAVERI.add(SRNP18M);listAMPT_KAVERI.add(SRNP6);
        listAMPT_KAVERI.add(ESI19);listAMPT_KAVERI.add(ESI2);
        listAMPT_KAVERI.add(SRNAGAR_pochammaTemple);
        listAMPT_KAVERI.add(ERG34);
        listAMPT_KAVERI.add(BTN20);listAMPT_KAVERI.add(BTN7);
        listAMPT_KAVERI.add(MSP27);listAMPT_KAVERI.add(MSP2);
        listAMPT_KAVERI.add(BLR17);listAMPT_KAVERI.add(BLR2);
        listAMPT_KAVERI.add(KUK40);listAMPT_KAVERI.add(KUK2);
        listAMPT_KAVERI.add(KPHB29);listAMPT_KAVERI.add(KPHB2);
        listAMPT_KAVERI.add(JNTU36);listAMPT_KAVERI.add(JNTU3);
        listAMPT_KAVERI.add(MYP46);listAMPT_KAVERI.add(MYP2);
        listAMPT_KAVERI.add(L1S1);
        listAMPT_KAVERI.add(alwynCross);
        listAMPT_KAVERI.add(Karachi_Chandanagar);
        listAMPT_KAVERI.add(kaveriHotel);
        listAMPT_KAVERI.add(greenfield);

        listAMPT_KAVERI.add(LBNagar);
        listAMPT_KAVERI.add(NehruRingRoad);
        listAMPT_KAVERI.add(Nalgonda);
        listAMPT_KAVERI.add(purimetla);


        S2LatLng ammerpet_latlang = S2LatLng.fromDegrees(ameerpetMetro.getLatitude(), ameerpetMetro.getLongitude());

        //METHOD-1
        ArrayList<S2CellId> cellIds = new ArrayList<S2CellId>();cellIds.add(S2CellId.fromLatLng(ammerpet_latlang).parent(10));
        S2CellUnion cellUnion = new S2CellUnion();
        cellUnion.initRawCellIds(cellIds);
        for(LatLangVO vo : listAMPT_KAVERI){
            S2Point point = S2LatLng.fromDegrees(vo.getLatitude(), vo.getLongitude()).toPoint();
            if(cellUnion.contains(point)){
                System.out.println("IN : "+vo.getName());
            } else{
                System.out.println("OUT : "+vo.getName());
            }
        }

        //METHOD-2
        S2Cell cell = new S2Cell(ammerpet_latlang);
        S2LatLngRect cellBound = cell.getRectBound();
        S2RegionCoverer coverer = new S2RegionCoverer();
        coverer.setLevelMod(1);
        //coverer.setMinLevel(4);
        coverer.setMaxLevel(9);
        //coverer.setMaxCells(100);
        S2CellUnion covering = coverer.getCovering(cellBound);
        /*for(LatLangVO vo : listAMPT_KAVERI){
            S2Point point = S2LatLng.fromDegrees(vo.getLatitude(), vo.getLongitude()).toPoint();
            if(covering.contains(point)){
                System.out.println("IN : "+vo.getName());
            } else{
                System.out.println("OUT : "+vo.getName());
            }
        }*/

        //METHOD-3
        S2Cell cellAmeerpet = new S2Cell(S2CellId.fromLatLng(ammerpet_latlang).parent(8));
//        for(LatLangVO vo : listAMPT_KAVERI){
//            S2Point point = S2LatLng.fromDegrees(vo.getLatitude(), vo.getLongitude()).toPoint();
//            if(cellAmeerpet.contains(point)){
//                System.out.println("IN : "+vo.getName());
//            } else{
//                System.out.println("OUT : "+vo.getName());
//            }
//        }
        //
    }

    //START https://medium.com/@self.maurya/lesser-known-things-about-googles-s2-fea42f852f67
//    private void S2CellIdsCoveringGivenRadius(double radius, double lat, double lng){
//        // LatLngFromDegrees returns a LatLng for the coordinates given in degrees
//        S2LatLng latLng = S2LatLng.fromDegrees(lat, lng);
//        // PointFromLatLng returns an Point for the given LatLng
//        S2Point s2Point = latLng.toPoint();
//        double EARTH_RADIUS_METERS = 6367000.0;
//        angle = new S1Angle(radius / EARTH_RADIUS_METERS);
//
//        // CapFromCenterAngle constructs a cap with the given center and angle
//        sphereCap := s2.CapFromCenterAngle(s2Point, angle)
//
//        region := s2.Region(sphereCap)
//
//        // Covering returns a CellUnion that covers the given region and satisfies the various restrictions
//        covering := s2s.rc.Covering(region)
//        return covering
//    }

//    func GetNearbyUsers(covering *s2.CellUnion, dataPoints map[string]*DataPoint) []string {
//        var userIds []int
//        for userId, dataPoint := range dataPoints {
//            latLng := s2.LatLngFromDegrees(dataPoint.point.Lat, dataPoint.point.Lng)
//            s2CellId := s2.CellIDFromLatLng(latLng)
//            if covering.ContainsCellID(s2CellId) {
//                userIds = append(userIds, userId)
//            }
//            return userIds
//        }

      //getDistance(final S2LatLng o, double radius)
    //END https://medium.com/@self.maurya/lesser-known-things-about-googles-s2-fea42f852f67



}
