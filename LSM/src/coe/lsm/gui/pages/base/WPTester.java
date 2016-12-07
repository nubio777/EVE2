package coe.lsm.gui.pages.base;


import coe.lsm.gui.pages.main.chart.PChart;
import coe.lsm.gui.pages.main.current.PCurrent;
import coe.lsm.gui.pages.main.map.PMap;
import coe.lsm.main.model.Team;
import org.apache.wicket.markup.html.panel.EmptyPanel;

/**
 * User: jrubio
 */
public class WPTester extends WPBase {

    public WPTester() {

//        add(new PChart("testPanel", Team.from("01")));
//        add(new PChart("testPanel"));
        add(new PMap("testPanel", null));
    }

    @Override
    public String getPageTitle() {
        return "LS16 - Test";
    }
}
