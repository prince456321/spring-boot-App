output "ec2_public_ip" {
  value = aws_instance.serveur_musique_app.public_ip
}

output "ec2_public_dns" {
  value = aws_instance.serveur_musique_app.public_dns
}

output "rds_endpoint" {
  value = aws_db_instance.mysql.address
}

output "app_url_hint" {
  value = "http://${aws_instance.serveur_musique_app.public_ip}:${var.app_port}"
}
