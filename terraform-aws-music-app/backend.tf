terraform {
  backend "s3" {
    bucket = "s3-demo-music"
    key    = "state/terraform.tfstate"
    region = "us-east-1"
    # dynamodb_table = "terraform-lock-table"  <-- Supprimé ou commenté
  }
}
