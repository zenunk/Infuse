#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Mon Mar  4 17:55:11 2019
@author: zekaaa

"""

import warnings
warnings.filterwarnings("ignore")

import numpy as np
import pandas as pd
from keras import layers
from keras.models import Model, Sequential
from keras.layers import Dense, Dropout, Activation, LSTM
from keras.layers import Input, Flatten, merge, Lambda, Dropout
from keras.wrappers.scikit_learn import KerasClassifier
from keras.layers.wrappers import TimeDistributed, Bidirectional
from keras.utils import np_utils, to_categorical
from keras.optimizers import Adam, RMSprop
from keras.layers import Conv1D, MaxPooling1D, Embedding
from keras.preprocessing.text import Tokenizer
from keras.preprocessing.sequence import pad_sequences
from keras.layers import Conv1D, MaxPooling1D, Embedding
from keras.layers.normalization import BatchNormalization
from sklearn.model_selection import train_test_split, KFold, GridSearchCV, StratifiedShuffleSplit,StratifiedKFold
from sklearn.metrics import confusion_matrix, classification_report, accuracy_score
from sklearn.preprocessing import LabelEncoder
from sklearn.pipeline import Pipeline
from sklearn.feature_extraction.text import TfidfVectorizer, CountVectorizer
import gensim
from gensim.models import Word2Vec
from gensim.utils import simple_preprocess
from gensim.models.keyedvectors import KeyedVectors
import matplotlib.pyplot as plt
import itertools
from nltk.stem.wordnet import WordNetLemmatizer
from nltk.tokenize import RegexpTokenizer
from sklearn.utils import shuffle


#%%

full_data = np.loadtxt('INFUSE_Top4.csv',delimiter=',',skiprows=1)

x = full_data[:,0:(len(full_data[1])-1)]
y = full_data[:,len(full_data[1])-1]

print(len(full_data[1])-1)


#%%
encoder = LabelEncoder()

encoder.fit(y)

encoded_y = encoder.transform(y)

# convert integers to dummy variables (i.e. one hot encoded)
dummy_y = np_utils.to_categorical(encoded_y)



#%%
#==============================================================================
# Define plot_history function
#==============================================================================
def plot_history(history):
    loss_list = [s for s in history.history.keys() if 'loss' in s and 'val' not in s]
    val_loss_list = [s for s in history.history.keys() if 'loss' in s and 'val' in s]
    acc_list = [s for s in history.history.keys() if 'acc' in s and 'val' not in s]
    val_acc_list = [s for s in history.history.keys() if 'acc' in s and 'val' in s]
    
    if len(loss_list) == 0:
        print('Loss is missing in history')
        return 
    
    ## As loss always exists
    epochs = range(1,len(history.history[loss_list[0]]) + 1)
    
    ## Loss
    plt.figure(1)
    for l in loss_list:
        plt.plot(epochs, history.history[l], 'b', label='Training loss (' + str(str(format(history.history[l][-1],'.5f'))+')'))
    for l in val_loss_list:
        plt.plot(epochs, history.history[l], 'g', label='Validation loss (' + str(str(format(history.history[l][-1],'.5f'))+')'))
    
    plt.title('Loss')
    plt.xlabel('Epochs')
    plt.ylabel('Loss')
    plt.legend()
    
    ## Accuracy
    plt.figure(2)
    for l in acc_list:
        plt.plot(epochs, history.history[l], 'b', label='Training accuracy (' + str(format(history.history[l][-1],'.5f'))+')')
    for l in val_acc_list:    
        plt.plot(epochs, history.history[l], 'g', label='Validation accuracy (' + str(format(history.history[l][-1],'.5f'))+')')

    plt.title('Accuracy')
    plt.xlabel('Epochs')
    plt.ylabel('Accuracy')
    plt.legend()
    plt.show()




#==============================================================================
# plot confusion_matrix function
#==============================================================================

def plot_confusion_matrix(cm, classes,
                          normalize=False,
                          cmap=plt.cm.Blues):
    """
    This function prints and plots the confusion matrix.
    Normalization can be applied by setting `normalize=True`.
    """
    if normalize:
        cm = cm.astype('float') / cm.sum(axis=1)[:, np.newaxis]
        title='Normalized confusion matrix'
    else:
        title='Confusion matrix'

    plt.imshow(cm, interpolation='nearest', cmap=cmap)
    plt.title(title)
    plt.colorbar()
    tick_marks = np.arange(len(classes))
    plt.xticks(tick_marks, classes, rotation=45)
    plt.yticks(tick_marks, classes)

    fmt = '.2f' if normalize else 'd'
    thresh = cm.max() / 2.
    for i, j in itertools.product(range(cm.shape[0]), range(cm.shape[1])):
        plt.text(j, i, format(cm[i, j], fmt),
                 horizontalalignment="center",
                 color="white" if cm[i, j] > thresh else "black")

    plt.tight_layout()
    plt.ylabel('True label')
    plt.xlabel('Predicted label')
    plt.show()
    
   

#==============================================================================
# Define full_multiclass_report which prints classification report
#==============================================================================    
## If binary (sigmoid output), set binary parameter to True
def full_multiclass_report(model,
                           x,
                           y_true,
                           classes,
                           batch_size=64,
                           binary=False):

    # 1. Transform one-hot encoded y_true into their class number
    if not binary:
        y_true = np.argmax(y_true,axis=1)
    
    # 2. Predict classes and stores in y_pred
    y_pred = model.predict_classes(x, batch_size=batch_size)
    
    # 3. Print accuracy score
    print("Accuracy : "+ str(accuracy_score(y_true,y_pred)))
    
    print("")
    
    # 4. Print classification report
    print("Classification Report")
    print(classification_report(y_true,y_pred,digits=4))    
    
  
    # 5. Plot confusion matrix
    cnf_matrix = confusion_matrix(y_true,y_pred)
    print(cnf_matrix)
    plot_confusion_matrix(cnf_matrix,classes=classes)

#==============================================================================
# Input parameters
#==============================================================================

seed =1000

x_train, x_test, y_train, y_test = train_test_split(x, dummy_y, train_size=0.7, random_state=seed)

x_train, x_val, y_train, y_val = train_test_split(x_train, y_train, train_size=0.7, random_state=seed)



Neurons = 1024
Baseline = 100
Top1 = 161
Top2 = 214
Top3 = 249
Top4 = 283
Top5 = 323

NoOfAtt=Top4

model = Sequential()
model.add(Dense(units=Neurons,input_shape=(NoOfAtt,), activation='relu'))
model.add(Dense(units=Neurons, activation='relu'))
model.add(Dense(units=Neurons, activation='relu'))
model.add(Dense(5, activation='softmax'))

model.summary()

model.compile(loss='categorical_crossentropy',optimizer='adam', metrics=['accuracy'])



#==============================================================================
# Evaluate model and print results
#==============================================================================

CNN_History=model.fit(x_train, y_train, epochs = 75, batch_size = 64,verbose=1, validation_data=(x_val,y_val), shuffle=True)

plot_history(CNN_History)

full_multiclass_report(model, x_val, y_val, encoder.inverse_transform(np.arange(5)))
