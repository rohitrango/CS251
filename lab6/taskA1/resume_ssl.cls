% resume.cls

% \documentstyle{resume}

% Inspired and drawn as an outline from Trey Hunner's distribution

\ProvidesClass{resume}[11/09/2016 v1 Resume class]

\LoadClass[11pt,letterpaper]{article}
\usepackage[export]{adjustbox}
\usepackage[margin=1in]{geometry}
\usepackage[parfill]{parskip} % Do not indent paragraphs
\usepackage{array} % required for boldface tabular columns
\usepackage{ifthen}
\usepackage{hyperref}
\usepackage[misc]{ifsym}
\usepackage{graphicx,wrapfig}
\usepackage{enumitem}

\nofiles % .aux files are not needed for resumes
\pagestyle{empty} % resumes do not need page numbers

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% HEADINGS: Commands for printing name and address

\def \name#1{\def\@name{#1}} % \name command can be used to set name
\def \@name {} % Set \@name to empty by default

\def \addressSep {$\diamond$} % Set default address seperator

% One or two address lines can be specified
\let \@addressone \relax
\let \@addresstwo \relax

% \address command can be used to set first and second address (optional)
\def \address #1{
  \@ifundefined{@addresstwo}{
    \def \@addresstwo {#1}
  }{
    \def \@addressone {#1}
  }
}

% \printaddress is used to style an address line (given as input)
\def \printaddress #1{
  \begingroup
    \def \\ {\addressSep\ }
    \centerline{#1}
  \endgroup
  \par
  \addressskip
}

% \printname is used to print the name as a page header
\def \printname {
  \begingroup
    \hfil{\MakeUppercase{\namesize\bf \@name}}\hfil
    \nameskip\break
  \endgroup
}

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% DOCUMENT: Create the head of the document

\let\ori@document=\document
\renewcommand{\document}{
  \ori@document % Begin document
  \printname % Print the name specified with \name
  \@ifundefined{@addressone}{}{ % Print the first address if specified
    \printaddress{\@addressone}}
  \@ifundefined{@addresstwo}{}{ % Print the second address if specified
    \printaddress{\@addresstwo}}
}

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% start writing your environments and commands from here

\newenvironment{Header}[7]
{
  \begin{wrapfigure}{r}{4cm}
  \vspace{-35pt}
  \label{wrap-fig:1}
  \includegraphics[width=3.5cm]{photo.jpg}
  \end{wrapfigure}

  \textbf{#1}\\[4pt]
  #2
  #3\\[4pt]
  #4\\
  #5\\[4pt]
  \Letter\,\href{mailto:#6}{#6} \\
  \rule{\textwidth}{1pt}
}


\newenvironment{Section}[1]
{
\Large
{
\uppercase{\textbf{{\em \underline{#1}}}}\\
}
}


\newenvironment{Subsection}[5]
{
\begin{table}[h]
\begin{tabular}{ll}
\textbf{#1} & \parbox[t]{0.8\textwidth}{\raggedleft\texttt{#2}}\par\smallskip \\
{\it #3} &   \parbox[t]{0.8\textwidth}{\raggedleft{\sc #4}}\par\smallskip \\
\end{tabular}
\end{table}
\begin{itemize}[leftmargin=6mm]
\normalsize
{
\vspace{-20pt}


#5
}
\end{itemize}
}


\newenvironment{Course}
{
  
}


\def\namesize{\huge}
\def\nameskip{\medskip}
\def\addressskip{\smallskip}
\def\sectionskip{\bigskip}
\def\sectionlineskip{\medskip}

