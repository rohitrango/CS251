.PHONY: help run mig mkmg shell doall

help:
	@echo
	@echo "make run : runserver"
	@echo "make mig: migrate"
	@echo "make mkmg: makemigrations"
	@echo "make shell: run shell"
	@echo "feel free to add new features and update the help section."
	@echo
run:
	@python3 manage.py runserver 8037 --insecure

mig:
	@python3 manage.py migrate

mkmg:
	@python3 manage.py makemigrations

shell:
	@python3 manage.py shell

doall:
	@python3 manage.py makemigrations
	@python3 manage.py migrate
	@python3 manage.py runserver 8037 --insecure



