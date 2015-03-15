package com.dimalimonov.tracking.service.impl;

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

import com.dimalimonov.tracking.dao.CarrierRestTemplate;
import com.dimalimonov.tracking.domain.Activity;
import com.dimalimonov.tracking.service.CarrierService;

@Service("upsService")
public class UpsServiceImpl implements CarrierService {

	private static final Logger logger = LoggerFactory.getLogger(UpsServiceImpl.class);

	@Autowired
	private CarrierRestTemplate upsRestTemplate = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dimalimonov.tracking.service.impl.UpsService#getActivityList(java
	 * .lang.String)
	 */
	@Override
	public List<Activity> getActivityList(String id) {
		String fullStatus = upsRestTemplate.getStatus(id);
		List<Activity> activityList = new ArrayList<Activity>();
		try {
			Document document = DocumentHelper.parseText(fullStatus);
			List<Element> activities = (List<Element>) document.selectNodes("/TrackResponse/Shipment/Package/Activity");
			for (Element activity : activities) {
				Activity a = new Activity();

				Element e = (Element) activity.selectSingleNode("ActivityLocation/Address/City");
				if (e != null) {
					a.setCity(e.getText());
				}
				e = (Element) activity.selectSingleNode("ActivityLocation/Address/StateProvinceCode");
				if (e != null) {
					a.setStateProvinceCode(e.getText());
				}
				e = (Element) activity.selectSingleNode("ActivityLocation/Address/PostalCode");
				if (e != null) {
					a.setPostalCode(e.getText());
				}

				e = (Element) activity.selectSingleNode("ActivityLocation/Address/CountryCode");
				if (e != null) {
					a.setCountryCode(e.getText());
				}

				e = (Element) activity.selectSingleNode("Status/StatusType/Description");
				a.setStatusDescription(e.getText());

				e = (Element) activity.selectSingleNode("Date");
				a.setDate(e.getText());

				e = (Element) activity.selectSingleNode("Time");
				a.setTime(e.getText());

				activityList.add(a);
			}
			logger.info("Total of {}", activities.size());
		} catch (DocumentException e) {
			logger.error(e.getMessage());
		}

		return activityList;
	}

}
