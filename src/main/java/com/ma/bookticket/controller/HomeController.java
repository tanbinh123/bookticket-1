package com.ma.bookticket.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ma.bookticket.pojo.Line;
import com.ma.bookticket.pojo.Trips;
import com.ma.bookticket.service.LineService;
import com.ma.bookticket.service.TripsService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 控制首页相关
 *
 * @author yong
 * @date 2021/1/20 0:03
 */
@Controller
@RequestMapping("/")
public class HomeController {

    private int pageSize=8;//表示每页展示的数据
    @Autowired
    private TripsService tripsService;
    @Autowired
    private LineService lineService;
    /**
     * 跳转到首页
     * @author yong
     * @date 2021/1/20 15:06
     * @return java.lang.String 页面逻辑名
     */
    @GetMapping
    public String toHome() {
        return "default/index";
    }

    /**
     * 分页操作获取信息
     * @param model 给页面传递参数
     * @author yong
     * @date 2021/1/22 20:40
     * @return java.lang.String
     */

    @GetMapping("/getTrips")
    public String home(Model model,@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum) {

        Session session= SecurityUtils.getSubject().getSession();
        //为了程序的严谨性，判断非空：
        if(pageNum == null){
            pageNum = 1;   //设置默认当前页
        }
        if(pageNum <= 0){
            pageNum = 1;
        }

        //获取页面的分页信息
        PageInfo<Trips> pageInfo= null;
        try {
            pageInfo = (PageInfo<Trips>)session.getAttribute("pageInfo");
        } catch (InvalidSessionException e) {
            e.printStackTrace();
        }
        assert pageInfo != null;
        Trips aTrip=pageInfo.getList().get(0);
        int line_id=aTrip.getTrips_line_id();
        Date date=(Date) session.getAttribute("date");
        String start_station=aTrip.getTrips_start_station_name();
        String end_station=aTrip.getTrips_end_station_name();
        List<Trips> someTrips;

        //1.引入分页插件,pageNum是第几页，pageSize是每页显示多少条,默认查询总数count
        PageHelper.startPage(pageNum,pageSize);
        //2.紧跟的查询就是一个分页查询-必须紧跟.后面的其他查询不会被分页，除非再次调用PageHelper.startPage
        someTrips = tripsService.getSomeTrips(line_id, date);//只含本页的数据
        if(someTrips!=null&&someTrips.size()!=0) {
            someTrips.forEach(trips -> {
                trips.setTrips_start_station_name(start_station);
                trips.setTrips_end_station_name(end_station);
            });
            try {
                //3.使用PageInfo包装查询后的结果
                PageInfo<Trips> pageInfo2 = new PageInfo<>(someTrips,pageSize);
                model.addAttribute("pageInfo",pageInfo2);
                session.setAttribute("date",date);
            }finally {
                PageHelper.clearPage(); //清理 ThreadLocal 存储的分页参数,保证线程安全
            }
        }
        return "default/index";
    }

    /**
     * 根据用户选择的条件获取特定的车次
     * @param start_station 起始站点名
     * @param end_station 到达站点名
     * @param datestr 用户选择的日期
     * @param model 存放参数
     * @author yong
     * @date 2021/1/22 1:21
     * @return java.lang.String
     */

    @PostMapping("/getTrips")
    public String getTrips(@RequestParam("StartStation") String start_station,
                           @RequestParam("EndStation") String end_station,
                           @RequestParam("date") String datestr, Model model) throws ParseException {

        Session session= SecurityUtils.getSubject().getSession();
        List<Trips> someTrips;
        if(datestr!=null&&datestr.length()!=0) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date=format.parse(datestr);
            Line line=lineService.getOne(start_station,end_station);
            int line_id;
            //分页
            int pageNum=1;//表示第几页
            if(line!=null) {
                line_id=line.getLine_id();
                //1.引入分页插件,pageNum是第几页，pageSize是每页显示多少条,默认查询总数count
                PageHelper.startPage(pageNum,pageSize);
                //2.紧跟的查询就是一个分页查询-必须紧跟.后面的其他查询不会被分页，除非再次调用PageHelper.startPage
                someTrips = tripsService.getSomeTrips(line_id, date);//只含本页的数据
                if(someTrips!=null&&someTrips.size()!=0) {
                    someTrips.forEach(trips -> {
                        trips.setTrips_start_station_name(start_station);
                        trips.setTrips_end_station_name(end_station);
                    });
                    try {
                        //3.使用PageInfo包装查询后的结果
                        PageInfo<Trips> pageInfo = new PageInfo<>(someTrips,pageSize);
                        model.addAttribute("pageInfo",pageInfo);
                        session.setAttribute("date",date);
                        session.setAttribute("pageInfo",pageInfo);
                    }finally {
                        PageHelper.clearPage(); //清理 ThreadLocal 存储的分页参数,保证线程安全
                    }
                }

            }
        }
        return "default/index";
    }
    /**
     * 跳转到确认订单页面，页面需包括车次的一些信息
     * @param trips_id 车次编号
     * @param model 给页面传递参数
     * @author yong
     * @date 2021/1/23 23:32
     * @return java.lang.String
     */

    @GetMapping("/user/confirm_order/{trips_id}")
    public String toConfirmOrder(@PathVariable("trips_id") Integer trips_id, Model model ) {

        Trips trips = tripsService.getOneById(trips_id);
        if(trips!=null) {
            int line_id=trips.getTrips_line_id();
            Line line = lineService.getById(line_id);
            if (line!=null) {
                String start_station=line.getLine_start_station_name();
                String end_station=line.getLine_end_station_name();
                trips.setTrips_start_station_name(start_station);
                trips.setTrips_end_station_name(end_station);
            }
        }
        model.addAttribute("trips",trips);
        return "default/confirm_order";
    }
}
