package com.dimalimonov.tracking.rest.template.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

@Service("fedexService")
public class FedexServiceImpl implements CarrierService {

	private static final Logger logger = LoggerFactory.getLogger(FedexServiceImpl.class);

	@Autowired
	private CarrierRestTemplate fedexRestTemplate = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dimalimonov.tracking.service.impl.UpsService#getActivityList(java
	 * .lang.String)
	 */
	@Override
	public List<Activity> getActivityList(String id) {
		String fullStatus = fedexRestTemplate.getStatus(id);
		List<Activity> activityList = new ArrayList<Activity>();
		try {
			Document document = DocumentHelper.parseText(fullStatus);
			List<Element> activities = (List<Element>) document.selectNodes("//*[name() = 'StatusDetail']");
			for (Element activity : activities) {
				Activity a = new Activity();

				Element e = (Element)  activity.selectSingleNode("*[name()='Location']").selectSingleNode("*[name()='City']");
				if (e != null) {
					a.setCity(e.getText());
				}
				e = (Element)  activity.selectSingleNode("*[name()='Location']").selectSingleNode("*[name()='StateOrProvinceCode']");
				if (e != null) {
					a.setStateProvinceCode(e.getText());
				}
				
				e = (Element) activity.selectSingleNode("*[name()='Location']").selectSingleNode("*[name()='CountryCode']");
				if (e != null) {
					a.setCountryCode(e.getText());
				}

				e = (Element) activity.selectSingleNode("*[name()='Description']");
				a.setStatusDescription(e.getText().toUpperCase());

				e = (Element) activity.selectSingleNode("*[name()='CreationTime']");
				
				if (e!= null) {
					DateFormat d = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
					try {
						Date parse = d.parse(e.getText());
						a.setDate(parse.toString());
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				activityList.add(a);
			}
			logger.info("Total of {}", activities.size());
		} catch (DocumentException e) {
			logger.error(e.getMessage());
		}

		return activityList;
	}

}
