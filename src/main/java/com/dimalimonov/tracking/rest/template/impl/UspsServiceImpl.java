package com.dimalimonov.tracking.rest.template.impl;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dimalimonov.tracking.domain.Activity;
import com.dimalimonov.tracking.rest.template.CarrierRestTemplate;
import com.dimalimonov.tracking.service.CarrierService;

@Service("uspsService")
public class UspsServiceImpl implements CarrierService {

	private static final Logger logger = LoggerFactory.getLogger(UspsServiceImpl.class);

	@Autowired
	private CarrierRestTemplate uspsRestTemplate = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dimalimonov.tracking.service.impl.UpsService#getActivityList(java
	 * .lang.String)
	 */
	@Override
	public List<Activity> getActivityList(String id) {
		String fullStatus = uspsRestTemplate.getStatus(id);

		List<Activity> activityList = new ArrayList<Activity>();
		try {
			Document document = DocumentHelper.parseText(fullStatus);

			Element statusAct = (Element) document.selectSingleNode("/TrackResponse/TrackInfo/TrackSummary");
			Activity status = new Activity();
			status.setStatusDescription(statusAct.getText());
			activityList.add(status);

			List<Element> activities = (List<Element>) document.selectNodes("/TrackResponse/TrackInfo/TrackDetail");
			for (Element activity : activities) {
				Activity a = new Activity();

				a.setStatusDescription(activity.getText());

				activityList.add(a);
			}
			logger.info("Total of {}", activities.size());
		} catch (DocumentException e) {
			logger.error(e.getMessage());
		}

		return activityList;
	}

}
