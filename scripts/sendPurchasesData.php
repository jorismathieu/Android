<?php
	try
	{
		$bdd = new PDO('mysql:host=localhost;dbname=superflux', 'root', '');
		
		$id = $_POST['id'];
		
		$reponse = $bdd->query('DELETE FROM flux WHERE id="'.$id.'"');
		
		if ($reponse == false)
			$success = 0;
		else
			$success = 1;
						
		$result['success'] = $success;
		if ($success == 1)
		{
			$result['message'] = "Adding flux with success " . $id;
		}
		else
			$result['message'] = "Error while connecting to the database";
			
		
		
	}
	catch (Exception $e)
	{
		$result['success'] = 0;
		$result['message'] = "Error while connecting to the database";
	}
	
	echo json_encode($result);
	
?>