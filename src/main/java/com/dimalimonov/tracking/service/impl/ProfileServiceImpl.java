package com.dimalimonov.tracking.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dimalimonov.tracking.domain.Carrier;
import com.dimalimonov.tracking.domain.Profile;
import com.dimalimonov.tracking.service.ProfileService;

@Service("profileService")
public class ProfileServiceImpl implements ProfileService {

	@Value("${usps.threshold}")
	public Integer uspsThreshold = null;

	@Value("${ups.threshold}")
	public Integer upsThreshold = null;

	@Override
	public Profile addDefaultSettings(Profile p) {

		if (p.getThreashold().get(Carrier.USPS.toString()) == null) {
			p.addCarrierThreshold(Carrier.USPS, uspsThreshold);
		}
		if (p.getThreashold().get(Carrier.UPS.toString()) == null) {
			p.addCarrierThreshold(Carrier.UPS, uspsThreshold);
		}
		p.setEmailOnNewOrders(true);
		p.setEmailOnOrderStateChanges(true);
		return p;
	}

	public Integer getUspsThreshold() {
		return uspsThreshold;
	}

	public Integer getUpsThreshold() {
		return upsThreshold;
	}

}
