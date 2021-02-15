package com.autonomous.pm.core.queue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import com.autonomous.pm.config.AppProperties;
import com.autonomous.pm.domain.structure.DrivingInfo;
import com.autonomous.pm.domain.structure.Event;
import com.autonomous.pm.domain.structure.RouteInfo;
import com.autonomous.pm.domain.structure.SensorInfo;
import com.autonomous.pm.domain.structure.TripInfo;
import com.autonomous.pm.domain.structure.mem.CommonMem;
import com.autonomous.pm.domain.structure.mem.DrivingInfoMem;
import com.autonomous.pm.domain.structure.mem.EventMem;
import com.autonomous.pm.domain.structure.mem.RouteInfoMem;
import com.autonomous.pm.domain.structure.mem.SensorInfoMem;
import com.autonomous.pm.domain.structure.mem.TripInfoMem;
import com.autonomous.pm.memcache.MemDB;
import com.autonomous.pm.model.Do.VVhcl;
import com.autonomous.pm.service.DrivingInfoService;
import com.autonomous.pm.service.EventService;
import com.autonomous.pm.service.RouteInfoService;
import com.autonomous.pm.service.SensorInfoService;
import com.autonomous.pm.service.TripInfoService;
import com.autonomous.pm.util.Environment;
import com.autonomous.pm.util.Timewatch;
import com.autonomous.pm.web.websocket.WebSocketUiServiceImpl;

@SpringBootApplication
@PropertySource(value = "classpath:application.properties")
public class BlockingController {
	public static final Logger logger = LoggerFactory.getLogger(BlockingController.class);
	public static final Logger wsFileLogger = LoggerFactory.getLogger("wsFileLogger");

	public Thread[] consum;// = new Thread[5];

	@Autowired
	private DrivingInfoService drivingInfoService;
	@Autowired
	private TripInfoService tripInfoService;
	@Autowired
	private SensorInfoService sensorInfoService;
	@Autowired
	private EventService eventService;
	@Autowired
	private RouteInfoService routeInfoService;
	
	@Autowired
    private WebSocketUiServiceImpl uiService;


	/**
	 *
	 */
	class Consumer implements Runnable {

		int index;

		public Consumer(int index) {
			this.index = index;
		}

		@Override
		public void run() {

			String qName = String.format("QNAME." + index);
			String threadName = AppProperties.instance().getProperty(qName);
			Thread.currentThread().setName("Q" + index + "." + threadName);
			
			logger.debug("qName:"+qName);
			
			List<CommonMem> memList = new ArrayList<CommonMem>();

			int loopStatus = -1;
			while (loopStatus < 0) {
//			while (true) {
				try {

					BlockingQueue<CommonMem> cQueue = GlobalQueue.INSTANCE.getPmMeseageQ(this.index);	// Current Queue
					memList.clear();
					
					int qsz2 = cQueue.size();
					CommonMem memObject = cQueue.take();
					memList.add(memObject);
					
					

					if (memObject == null) {
						Thread.sleep(50);
						continue;
					}
					while ( cQueue.size() > 0 ) { // 아직 Queue 에 데이터가 더 있을 경우
						memList.add(cQueue.take());
					}
					int qsz = cQueue.size();
					wsFileLogger.info("Q [{}] GET Size:{}->{}", index, qsz2, qsz);
					
					Long idV = memObject.getIdV();

					if (memObject instanceof DrivingInfoMem) {
						
						if (Environment.instance().isUseServiceLayer()) {
							Timewatch tw = new Timewatch();
							try {
								List<DrivingInfoMem> wdiList = new ArrayList<DrivingInfoMem>();
								for( CommonMem wo : memList ) {
									wdiList.add((DrivingInfoMem) wo);
								}
								drivingInfoService.insertInfo(wdiList);
							} catch (RuntimeException ex) {
								logger.error("[Exception] drivingInfoService.insertInfo(): {}", ex.toString());
							}finally {
								logger.debug("drivingInfoService.insertInfo(): {} ms",tw.stopMs());
							}
						}
						
					} else if (memObject instanceof TripInfoMem) {
						
						if (Environment.instance().isUseServiceLayer()) {
							Timewatch tw = new Timewatch();
							try {
								List<TripInfoMem> wtiList = new ArrayList<TripInfoMem>();
								for( CommonMem wo : memList ) {
									wtiList.add((TripInfoMem) wo);
								}
								tripInfoService.insertInfo(wtiList);
							} catch (RuntimeException ex) {
								logger.error("[Exception] tripInfoService.insertInfo(): {}", ex.toString());
							}finally {
								logger.debug("tripInfoService.insertInfo(): {} ms",tw.stopMs());
							}
						}
						
					} else if (memObject instanceof SensorInfoMem) {
						
						if (Environment.instance().isUseServiceLayer()) {
							Timewatch tw = new Timewatch();
							try {
								List<SensorInfoMem> wsiList = new ArrayList<SensorInfoMem>();
								for( CommonMem wo : memList ) {
									wsiList.add((SensorInfoMem) wo);
								}
								sensorInfoService.insertInfo(wsiList);
							} catch (RuntimeException ex) {
								logger.error("[Exception] sensorInfoService.insertInfo(): {}", ex.toString());
							}finally {
								logger.debug("sensorInfoService.insertInfo(): {} ms",tw.stopMs());
							}
						}
						
					} else if (memObject instanceof EventMem) {
						
						if (Environment.instance().isUseServiceLayer()) {
							Timewatch tw = new Timewatch();
							try {
								List<EventMem> weList = new ArrayList<EventMem>();
								for( CommonMem wo : memList ) {
									weList.add((EventMem) wo);
								}
								// DB저장
								eventService.insertInfo(weList);
								
								Date ts = ((EventMem)memObject).getData().getTs();
								logger.info(">>>>EVENT DB insert, Event.ts={}", ts.getTime());
								
								// UI에 메세지를 전송한다.
								MemDB.EVT.insertSafety(idV, (EventMem)memObject);
								uiService.sendMessage();
								logger.info(">>>>EVENT UI Send, Event.ts={}", ts.getTime());
								
							} catch (RuntimeException ex) {
								logger.error("[Exception] eventService.insertInfo(): {}", ex.toString());
							}finally {
								logger.debug("eventService.insertInfo(): {} ms",tw.stopMs());
							}
						}
						
					} else if (memObject instanceof RouteInfoMem) {
						
						if (Environment.instance().isUseServiceLayer()) {
							Timewatch tw = new Timewatch();
							try {
								List<RouteInfoMem> wriList = new ArrayList<RouteInfoMem>();
								for( CommonMem wo : memList ) {
									wriList.add((RouteInfoMem) wo);
								}
								routeInfoService.insertInfo(wriList);
							} catch (RuntimeException ex) {
								logger.error("[Exception] routeInfoService.insertInfo(): {}", ex.toString());
							}finally {
								logger.debug("routeInfoService.insertInfo(): {} ms",tw.stopMs());
							}
						}
						
					} else {
						logger.info("Recived memObject UNHANDLED: " + memObject.getClass().getSimpleName());
					}

				} catch (InterruptedException ex) {
					logger.error(ex.toString());
				}
			}
		}

	}

	public void start() {

		// BlockingQueue사용 방법
		// ex1)BlockingQueue<String> queue = new ArrayBlockingQueue<String>(큐의 맥스 사이즈);
		// ex2)BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
		// - 2개의 Thread사용
		// - 최대 4개 Thread사용 (LinkedBlockingQueue에서는 maximumPoolSize 값은 의미 없음 )
		// - 60초의 Timeout 설정
		// - 큐는 LinkedBlockingQueue
		// ThreadPoolExecutor threadPool = new ThreadPoolExecutor( 2, 4, 60,
		// TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>() );
		
		logger.info(">>>>>>>>>>>>>>>>>>>>>BlockingController.start()<<<<<<<<<<<<<<<<<<<<<<");

		int queueCount = AppProperties.instance().getPropertyInt("QINDEX.Total");

		consum = new Thread[queueCount];

		for (int i = 0; i < queueCount; i++) {
			consum[i] = new Thread(new Consumer(i));
			// consum[i].setName("VQ" + i + "." + threadName[i]);
			consum[i].start();
		}
	}

}
