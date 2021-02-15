/**
 * 
 */
package com.autonomous.pm.memcache.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.autonomous.pm.memcache.base.MemEntity;
import com.autonomous.pm.model.Do.TDrvRawYymm;
import com.autonomous.pm.model.Do.TSnsrRawYymm;
import com.autonomous.pm.model.Do.TTrpRaw;
import com.autonomous.pm.model.Do.TVhcl;

/**
 *
 */
public class MemVehicleDrv {
	public static final Logger logger = LoggerFactory.getLogger(MemVehicleDrv.class);
	
	public MemEntity<TVhcl> vehicleInfo = new MemEntity<TVhcl>(3);
	public MemEntity<TTrpRaw> tripInfo = new MemEntity<TTrpRaw>(3);
	public MemEntity<TDrvRawYymm> driveInfo = new MemEntity<TDrvRawYymm>(3);
	public MemEntity<TSnsrRawYymm> sensorInfo = new MemEntity<TSnsrRawYymm>(3);
	
}
