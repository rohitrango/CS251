% resume.cls

% \documentstyle{resume}

% Inspired and drawn as an outline from Trey Hunner's distribution

\ProvidesClass{resume}[11/09/2016 v1 Resume class]

\LoadClass[11pt,letterpaper]{article}
\usepackage[export]{adjustbox}
\usepackage[left=1in,right=1in,top=0.8in, bottom=1in]{geometry}
\usepackage[parfill]{parskip} % Do not indent paragraphs
\usepackage{array} % required for boldface tabular columns
\usepackage{ifthen}
\usepackage{hyperref}
\usepackage[misc]{ifsym}
\usepackage{graphicx,wrapfig}
\usepackage{enumitem}
\usepackage{xparse}
\usepackage{ifthen}

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
\begin{minipage}[t]{0.48\textwidth}

\vspace{-20pt}

\ifthenelse{\equal{#1}{}}{}{\textbf{#1}\\[4pt]}
  \ifthenelse{\equal{#2}{}}
  {
  \ifthenelse{\equal{#3}{}}{}{#3\\[4pt]}
  }{#2 #3\\[4pt]}
  \ifthenelse{\equal{#4}{}}{}{#4\\}
  \ifthenelse{\equal{#5}{}}{}{#5\\[4pt]}
  \ifthenelse{\equal{#6}{}}{}{\Letter\,\href{mailto:#6}{#6} \\}
\end{minipage}
\hfill
\begin{minipage}[t]{0.48\textwidth}
\begin{flushright}
\vspace{-35pt}
\ifthenelse{\equal{#7}{}}{}{\includegraphics[width=4cm]{#7}}
\end{flushright}
\end{minipage}

  % \begin{wrapfigure}{r}{4cm}
  % \label{wrap-fig:1}
  % \includegraphics[width=3.5cm]{photo.jpg}
  % \end{wrapfigure}
}

\newcommand{\cvitem}[2]{
\normalsize
{
\ifthenelse{\equal{#1}{}}{\item #2}{\item #2(\textbf{{\it #1}})}
}
}

\newenvironment{Section}[1]
{
\Large
{
\ifthenelse{\equal{#1}{}}{}{\uppercase{\textbf{{\em \underline{#1\\}}}}\\}
}
}


\newenvironment{Subsection}[5]
{

\large
{
\begin{minipage}[t]{0.48\textwidth}
\ifthenelse{\equal{#1}{}}{
  \ifthenelse{\equal{#2}{}}{}{\textbf{#1}\\}
}{\textbf{#1}\\}
{\it #3}
\end{minipage}
\hfill
\begin{minipage}[t]{0.48\textwidth}
\begin{flushright}
\ifthenelse{\equal{#2}{}}{
  \ifthenelse{\equal{#1}{}}{}{\texttt{#2}\\}
}{\texttt{#2}\\}
{\sc #4}
\end{flushright}
\end{minipage}
}
\ifthenelse{\equal{#1}{}}{
  \ifthenelse{\equal{#2}{}}{}{\vspace{10pt}}
}{\vspace{10pt}}
\ifthenelse{\equal{#3}{}}{
  \ifthenelse{\equal{#4}{}}{}{\vspace{10pt}}
}{\vspace{10pt}}
% \begin{table}[h]
% \begin{tabular}{ll}
% \textbf{#1} & \parbox[t]{0.8\textwidth}{\raggedleft\texttt{#2}}\par\smallskip \\
% {\it #3} &   \parbox[t]{\textwidth}{\raggedleft{\sc #4}}\par\smallskip \\
% \end{tabular}
% \end{table}
% \ifthenelse{\equal{#5}{}}{}
% {

% \begin{itemize}[leftmargin=6mm]
% \normalsize
% {
% \vspace{-20pt}

% #5
% }
% \end{itemize}

% }
\if\relax\detokenize{#5}\relax\else
\vspace{-20pt}
    \begin{itemize}[nosep]
    #5
    \end{itemize}
  \fi
}

% \newcommand{\core}[1]{\renewcommand{\cores}{#1}}
% \newcommand{\noncore}[1]{\renewcommand{\noncores}{#1}}
% \newcommand{\cores}{}
% \newcommand{\noncores}{}

% \NewDocumentCommand\core{>{\SplitList{;}}m}
%   {
%     \begin{itemize}[label={}]
%       \ProcessList{#1}{ \insertitem }
%     \end{itemize}
%   }
% \newcommand\insertitem[1]{\item #1}

% \NewDocumentCommand\noncore{>{\SplitList{;}}m}
%   {
%     \begin{itemize}[label={}]
%       \ProcessList{#1}{ \insertitem }
%     \end{itemize}
%   }
% \renewcommand\insertitem[1]{\item #1}

\newcommand{\core}[1]
{
\begin{center}

% \vspace{-80pt}

\begin{minipage}[t]{0.2\textwidth}
\large
{
  \textbf{Core} \\
}
#1
\end{minipage}
\begin{minipage}[t]{0.3\textwidth}
\begin{flushright}
}

\newcommand{\noncore}[1]
{
\large
{
  \textbf{Non-Core} \\
}
#1
\end{flushright}
\end{minipage}
\end{center}

\vspace{10pt}
}

\newenvironment{Course}{
\Large
{
  \uppercase{\textbf{{\em \underline{Courses Taken}}}}\\
}
}
% {\begin{flushleft}}
% {
% \begin{table}[h]
% \begin{tabular}{ll}
% \textbf{##1} & \parbox[t]{0.8\textwidth}{\raggedleft\texttt{}}\par\smallskip \\
% \end{tabular}
% \end{table}
%   % \textbf{\core}\\
%   % \textbf{\noncore}
% \end{flushleft}}

\renewcommand{\quote}[2]{

{\centering

``{\it #1}" - {\sc #2}

}
}


\def\namesize{\huge}
\def\nameskip{\medskip}
\def\addressskip{\smallskip}
\def\sectionskip{\bigskip}
\def\sectionlineskip{\medskip}

