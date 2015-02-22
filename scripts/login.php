<?php
	try
	{
		$bdd = new PDO('mysql:host=localhost;dbname=petshop', 'root', '');
		
		$email = $_POST['Email'];
		$password = $_POST['Passwd'];
		$success = 0;
		$loginExists = 0;
		
		$reponse = $bdd->query('SELECT * FROM users');
		while ($donnees = $reponse->fetch())
		{
			if ($donnees['email'] == $email && $donnees['password'] == $password)
			{
				$success = 1;
				$id_user = $donnees['id'];
			}
			else if ($donnees['email'] == $email)
				$loginExists = 1;
			
		}
		
		$reponse->closeCursor();
		
		$result['success'] = $success;
		if ($success == 1)
		{
			$result['message'] = "Connection succeed";
			$result['id_user'] = $id_user;
		}
		else if ($loginExists == 0)
		{
			$request = "INSERT INTO users (email,password) VALUES (:email,:password)";
			$reponse = $bdd->prepare($request);
			$reponse->execute(array(':email'=>$email,
									':password'=>$password));

			$id_user = $bdd->lastInsertId();
			$reponse->closeCursor();
			
			$result['success'] = 1;
			$result['message'] = "Account successfully created";
			$result['id_user'] = $id_user;
		}
		else
		{
			$result['message'] = "Connection failed : password incorrect";
		}
		
	}
	catch (Exception $e)
	{
		$result['success'] = 0;
		$result['message'] = "Error while connecting to the database";
	}
	
	echo json_encode($result);
	
?>