package org.djd.busntrain;

public interface Readme {

  /*
    ==== train =====
    key=508fc434e3144362839cc82709c6365a

  rt values:{
  Red = Red Line (Howard-95th/Dan Ryan service)
  Blue = Blue Line (O‖Hare-Forest Park service)
  Brn = Brown Line (Kimball-Loop service)
  G = Green Line (Harlem/Lake-Ashland/63rd-Cottage Grove service)
  Org = Orange Line (Midway-Loop service)
  P = Purple Line (Linden-Howard shuttle service)
  Pink = Pink Line (54th/Cermak-Loop service)
  Y = Yellow Line (Skokie-Howard [Skokie Swift] shuttle service)
 }

 http://lapi.transitchicago.com/api/1.0/ttarrivals.aspx?key=508fc434e3144362839cc82709c6365a&max=1&mapid=40380

 mapid:40380 (Clark/Lake)
 rt:Blue (Blue Line only)
 http://lapi.transitchicago.com/api/1.0/ttarrivals.aspx?key=508fc434e3144362839cc82709c6365a&mapid=40380&rt=Blue

 stopid to display just one direction
 stpid:30375 Clark/Lake O'Hare
 http://lapi.transitchicago.com/api/1.0/ttarrivals.aspx?key=508fc434e3144362839cc82709c6365a&stpid=30375&rt=Blue


    [waitTime = arrT - prdt]

    cta_L_stops.csv file:file:///home/acorn/Downloads/cta_L_stops.zip
    ==== bus =====
     Sample request URLs:
 http://www.ctabustracker.com/bustime/api/v1/gettime?key=PcgY9BruYFVN9ZbRtB3qKbeU3

 [cacheable]
 [Input: N/A]
 [Output: route(rt)]
 http://www.ctabustracker.com/bustime/api/v1/getroutes?key=PcgY9BruYFVN9ZbRtB3qKbeU3

 [optional]
 [Input: rt=49]
 http://www.ctabustracker.com/bustime/api/v1/getvehicles?rt=49&key=PcgY9BruYFVN9ZbRtB3qKbeU3

 [Input: rt=49]
 [Output: direction(dir)]
 http://www.ctabustracker.com/bustime/api/v1/getdirections?rt=49&key=PcgY9BruYFVN9ZbRtB3qKbeU3

 [Input: rt=49 & dir=South Bound]
 [Output: stop Id]
 http://www.ctabustracker.com/bustime/api/v1/getstops?rt=49&dir=South
 Bound&key=PcgY9BruYFVN9ZbRtB3qKbeU3

 [optional]
 [Input: rt=49]
 [Output: geo-locational points that can be used to draw routes on a map]
 http://www.ctabustracker.com/bustime/api/v1/getpatterns?rt=49&key=PcgY9BruYFVN9ZbRtB3qKbeU3

 [Input: stpid=8214 & rt=49 ]
 http://www.ctabustracker.com/bustime/api/v1/getpredictions?stpid=8214&rt=49&key=PcgY9BruYFVN9ZbRtB3qKbeU3
 http://www.ctabustracker.com/bustime/api/v1/getpredictions?stpid=8214&key=PcgY9BruYFVN9ZbRtB3qKbeU3


 [optional]
 [Input: rt=49 | stpid=459]
 [Output: service bulletins]
 http://www.ctabustracker.com/bustime/api/v1/getservicebulletins?stpid=456&key=PcgY9BruYFVN9ZbRtB3qKbeU3
 http://www.ctabustracker.com/bustime/api/v1/getservicebulletins?rt=49&key=PcgY9BruYFVN9ZbRtB3qKbeU3

    */
}
