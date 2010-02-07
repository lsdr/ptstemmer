from distutils.core import setup

setup(name='PTStemmer',
      version='2.0',
      description='PTStemmer - A Stemming Toolkit for the Portuguese Language',
      author='Pedro Oliveira',
      author_email='',
      url='http://code.google.com/p/ptstemmer/',
      packages=['ptstemmer', 'ptstemmer.support', 'ptstemmer.support.datastructures', 'ptstemmer.exceptions', 'ptstemmer.implementations'],
      package_dir = {'': 'src'},
      package_data={'ptstemmer.implementations': ['*.xml']}
      )

