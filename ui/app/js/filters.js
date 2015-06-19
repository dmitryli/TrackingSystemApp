'use strict';

var ptFilters = angular.module('ptFilters', []);

ptFilters.filter('DeliveryStatus', function () {
	return function (deliveries, status) {
		var filtered = [],
			d, i;

		if (deliveries && deliveries.length > 0) {

			switch (status) {
			case 'overdue':
				for (i = 0; i < deliveries.length; i++) {
					d = deliveries[i];
					if (d.state === 'ACTIVE' && d.overDue) {
						filtered.push(d);
					}
				}
				break;

			case 'on-track':
				for (i = 0; i < deliveries.length; i++) {
					d = deliveries[i];
					if (d.state === 'ACTIVE' && !d.overDue) {
						filtered.push(d);
					}
				}
				break;

			case 'archived':
				for (i = 0; i < deliveries.length; i++) {
					d = deliveries[i];
					if (d.state === 'ARCHIVED') {
						filtered.push(d);
					}
				}
				break;
			}
		}

		return filtered;
	};
});

ptFilters.filter('StatusFormat', function() {
	return function(status) {
		switch(status){
			case 'INTRANSIT': return 'In-Transit';
			case 'DELIVERED': return 'Delivered';
			case 'UNKNOWN': return 'Unknown';
		}
	};
});