from __future__ import unicode_literals

from django.contrib.auth.models import User
from django.db import models

# Create your models here.

CHOICES = (
    ('ST', 'Student'),
    ('PR', 'Professor'),
    ('TA', 'Teaching Assistant'),
    ('AD', 'Admin'),
)

## Member type which specifies member type
class Member(models.Model):
	user = models.OneToOneField(User, on_delete=models.CASCADE)
	fullname = models.CharField(max_length=30)
	# Member type
	token = models.TextField(null=True,blank=True,default="-1")
	mtype = models.CharField(choices=CHOICES, default="ST",max_length=25)
	
	def __str__(self):
		return self.fullname

## Course details
class Course(models.Model):
	name = models.TextField()
	members = models.ManyToManyField(Member,blank=True)
	# assignments are added as foreign keys
	semester = models.IntegerField()
	added = models.DateField(auto_now_add=True,auto_now=False)
	course_code = models.CharField(max_length=6)

	def __str__(self):
		return self.course_code
##############################################################################

QUESTION_TYPES = (
	('MCQ','Multiple Choice Question'),
	('SHORT','Short answer type Question'),
	('RATE','Rating Type Question'),
)

## Contains the feedback for the current course
class Feedback(models.Model): 
	name = models.TextField()
	deadline = models.DateTimeField(blank=False)
	course = models.ForeignKey(Course)
	students = models.ManyToManyField(Member,blank=True)

	def __str__(self):
		return self.name

	class Meta:
		ordering = ['-deadline']


## Feedback Questions and answers
class FeedbackQuestion(models.Model):
	question = models.TextField()
	feedback = models.ForeignKey(Feedback)
	q_type = models.CharField(choices=QUESTION_TYPES, default="RATE",max_length=5)

	def __str__(self):
		return self.question


## Answer type to question given by users
## Each question will have 5 instances of this connected to it.
## Each one will store the rating number and the votes given to it
class FeedbackRatingAnswer(models.Model):
	q = models.ForeignKey(FeedbackQuestion)
	rating = models.IntegerField(default=0)
	votes = models.IntegerField(default=0)

	def __str__(self):
		return self.q.question

## Feedback Multi choice answer
## Each answer given through the app will increase its votes_count by 1
class FeedbackMCQChoice(models.Model):
	text = models.TextField()
	q = models.ForeignKey(FeedbackQuestion)
	votes_count = models.IntegerField(default=0)

	def __str__(self):
		return self.text

## Each short answer added will get inserted and will be stored in the answer to the feedback question
class FeedbackShortAnswer(models.Model):
	q = models.ForeignKey(FeedbackQuestion)
	text = models.TextField()

	def __str__(self):
		return self.text

##	Feedback form -> Questions
##	Question -> Rating / MCQ / Short Answer


###############################################################################
## Contains the assignment details for the current course
class Assignment(models.Model):
	name = models.TextField()
	description = models.TextField()
	deadline = models.DateTimeField(blank=False)
	course = models.ForeignKey(Course)

	def __str__(self):
		return self.name + " - " + self.course.course_code

	class Meta:
		ordering = ['-deadline']
