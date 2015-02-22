<?php
	try
	{
		$bdd = new PDO('mysql:host=localhost;dbname=petshop', 'root', '');
		
		if (isset($_POST['userId']))
			$userId = $_POST['userId'];
		
		$animalList = Array();
		$reponse = $bdd->query('SELECT id, type, name, description, address, coordinates FROM animals');

		while ($donnees = $reponse->fetch())
		{			
			$animal['id'] = $donnees['id'];
			$animal['type'] = $donnees['type'];
			$animal['name'] = $donnees['name'];
			$animal['description'] = $donnees['description'];
			$animal['address'] = $donnees['address'];
			$animal['coordinates'] = $donnees['coordinates'];
			array_push($animalList, $animal);
		}

		if ($reponse == false)
			$success = 0;
		else
			$success = 1;
			
		$reponse->closeCursor();
		
		$result['success'] = $success;
		if ($success == 1)
		{
			$result['message'] = "Getting flux with success";
			$result['animalList'] = $animalList;
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