$(document).ready( function () {
	 var table = $('#person').DataTable({
			"sAjaxSource": "/persons",
			"sAjaxDataProp": "",
			"order": [[ 0, "asc" ]],
			"aoColumns": [
			    { "mData": "name"},
		      { "mData": "dob" },
				  { "mData": "gender" },
				  { "mData": "email" },
				  { "mData": "mobile_no" },
				  { "mData": "country" },
				  { "mData": "state" },
				  { "mData": "pincode" },
				  { "mData": "image" }
				  
			]
	 })
});