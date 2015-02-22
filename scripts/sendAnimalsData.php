<?php
	try
	{
		$bdd = new PDO('mysql:host=localhost;dbname=petshop', 'root', '');
		
		$animals = $_POST['animals'];
		
		if (isset($animals) && empty($animals) == false)
		{
			$reponse = $bdd->query('TRUNCATE TABLE animals');
			$animal_list = explode("#", $animals);
			for ($i = 0; $i < count($animal_list); $i++)
			{
				if (empty($animal_list[$i]) == false)
				{
					$datas = explode('|', $animal_list[$i]);
					$reponse = $bdd->query('INSERT INTO animals(type, name, description, address, coordinates) VALUES ("'.$datas[0].'", "'.$datas[1].'", "'.$datas[2].'", "'.$datas[3].'", "'.$datas[4].'") ');
				}
			}
		}

		if ($reponse == false)
			$success = 0;
		else
			$success = 1;
						
		$result['success'] = $success;
		if ($success == 1)
		{
			$result['message'] = "Adding flux with success";
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