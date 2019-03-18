
"""
Created on Sun Mar 17 18:37:17 2019

@author: zekaaa
"""


import warnings
warnings.filterwarnings("ignore")

from keras.utils import np_utils, to_categorical
from sklearn.metrics import confusion_matrix, classification_report
from sklearn.preprocessing import LabelEncoder
from sklearn.feature_extraction.text import TfidfVectorizer, CountVectorizer
from sklearn.model_selection import train_test_split
from sklearn.model_selection import cross_val_score
from sklearn.tree import DecisionTreeClassifier  
from sklearn.naive_bayes import MultinomialNB
from sklearn.ensemble import AdaBoostClassifier
from xgboost import XGBClassifier
from sklearn.svm import SVC
from numpy import loadtxt




#==============================================================================
# Load the dataset
#==============================================================================
#%%


import numpy as np

full_data = np.loadtxt('INFUSE_Baseline.csv',delimiter=',',skiprows=1)

x = full_data[:,0:(len(full_data[1])-1)]
y = full_data[:,len(full_data[1])-1]

print(len(full_data[1])-1)

 


#==============================================================================
# Encode class values as integers 
#==============================================================================
encoder = LabelEncoder()
encoder.fit(y)
encoded_y = encoder.transform(y)
dummy_y = np_utils.to_categorical(encoded_y)



#==============================================================================
# Training and testing 
#==============================================================================
seed =1000

x_train, x_test, y_train, y_test = train_test_split(x, encoded_y, train_size=0.7, random_state=seed)

x_train, x_val, y_train, y_val = train_test_split(x_train, y_train, train_size=0.7, random_state=seed)


#==============================================================================
# SVM classifier
#==============================================================================
print("\nResults from SVM\n")

clf_SVM=SVC(C=1, kernel='linear')

clf_SVM.fit(x_train, y_train)

accuracy=clf_SVM.score(x_val, y_val)

y_pred = clf_SVM.predict(x_val)

print(confusion_matrix(y_val, y_pred))  

print(classification_report(y_val, y_pred, digits=4))
   
print("SVM accuracy:", accuracy)


#==============================================================================
# Decision Tree classifier
#==============================================================================
print("\n\n\nResults from Decision Tree\n")

clf_DT = DecisionTreeClassifier() 
 
clf_DT.fit(x_train, y_train)

accuracy=clf_DT.score(x_val, y_val)

y_pred = clf_DT.predict(x_val)


print(confusion_matrix(y_val, y_pred))  

print(classification_report(y_val, y_pred, digits=4))
   
print("DT accuracy:", accuracy)



#==============================================================================
# Naive Bayes classifier
#==============================================================================
print("\n\n\nResults from Naive Bayes\n")

clf_NB = MultinomialNB() 
 
clf_NB.fit(x_train, y_train)

accuracy=clf_NB.score(x_val, y_val)

y_pred = clf_NB.predict(x_val)

print(confusion_matrix(y_val, y_pred))  

print(classification_report(y_val, y_pred, digits=4))
  
print("NB accuracy:", accuracy)

