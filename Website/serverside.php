<?php
 
/*
 * DataTables example server-side processing script.
 *
 * Please note that this script is intentionally extremely simply to show how
 * server-side processing can be implemented, and probably shouldn't be used as
 * the basis for a large complex system. It is suitable for simple use cases as
 * for learning.
 *
 * See http://datatables.net/usage/server-side for full details on the server-
 * side processing requirements of DataTables.
 *
 * @license MIT - http://datatables.net/license_mit
 */
 
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Easy set variables
 */
 
// DB table to use
$table = 'courses';
 
// Table's primary key
$primaryKey = 'course_id';
 
// Array of database columns which should be read and sent back to DataTables.
// The `db` parameter represents the column name in the database, while the `dt`
// parameter represents the DataTables column identifier. In this case simple
// indexes
$columns = array(
	array( 
		'db' => 'course_id', 
		'dt' => 0, 
		'formatter' => function( $d, $row ) {
			return '<td><button id="favorites" onclick="addToFavorites(' . $d . ', 1)">Add</button></td>';
		} 
	),
	array( 
		'db' => 'course_image',
		'dt' => 1,
		'formatter' => function( $d, $row ) {
			return '<td><image src="' . $d . '" alt="missing course image" height="100" width="100"></td>';
		} 
	),
	array(
		'db' => 'title',
		'dt' => 2,
		'formatter' => function( $d, $row ) {
			return '<td width="500px"><a href="' . 'page.php?ref=' . $row['id'] . '" target="_blank">' . $row['title'] . '</a></td>';
		} 
	),
	array(
		'db' => 'rate',
		'dt' => 3,
		'formatter' => function( $d, $row ) {
			return '<td nowrap><input style="
								background-image: url(images/thumbs_up.png);
								background-color: transparent; 
								background-repeat: no-repeat; 
								background-position: 0px 0px; 
								border: none;  
								cursor: pointer;    
								height: 24px; 
								padding-left: 24px;   
								vertical-align: middle; 
								" 
								type="button" onclick="rate(' . 
								$row['course_id'] . ', 1)"/><span style="margin-right: 8px;" class="rate_count_' .  
								$row['course_id'] . '">' . $row['rate'] . 
								'</span><input style="
								background-image: url(images/thumbs_down.png);
								background-color: transparent; 
								background-repeat: no-repeat; 
								background-position: 0px 0px; 
								border: none;  
								cursor: pointer;    
								height: 24px; 
								padding-left: 24px;   
								vertical-align: middle; 
								" type="button" onclick="rate(' . 
								$row['course_id'] . ', -1)"/></td>';
		} 
	),
	array(
		'db' => 'start_date',
		'dt' => 4,
		'formatter' => function( $d, $row ) {
			return "<td>" . $row['start_date'] . "</td>";
		} 
	),
	array(
		'db' => 'course_length',
		'dt' => 5,
		'formatter' => function( $d, $row ) {
			return "<td>" . $row['course_length'] . "</td>";
		} 
	),
	array(
		'db' => 'site',
		'dt' => 6,
		'formatter' => function( $d, $row ) {
			return "<td>" . $row['site'] . "</td>";
		} 
	),
	array(
		'db' => 'profname',
		'dt' => 7,
		'formatter' => function( $d, $row ) {
			return "<td>" . $row['profname'] . "</td>";
		} 
	),
	array(
		'db' => 'profimage',
		'dt' => 8,
		'formatter' => function( $d, $row ) {
			return '<td><image src="' . $row['profimage'] . '" alt="missing image" height="100" width="100"></td>';
		} 
	)
);

// SQL server connection information
$sql_details = array(
    'user' => 'sjsucsor_s2g414s',
    'pass' => 'abcd#1234',
    'db'   => 'sjsucsor_160s2g42014s',
    'host' => 'localhost'
);

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * If you just want to use the basic configuration for DataTables with PHP
 * server-side, there is no need to edit below this line.
 */
 
require( 'ssp.class.php' );
 
echo json_encode(
    SSP::simple( $_GET, $sql_details, $table, $primaryKey, $columns )
);
?>
