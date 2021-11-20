// SPIM S20 MIPS simulator.
// Definitions for the SPIM S20.
//
// Copyright (C) 1990-2000 by James Larus (larus@cs.wisc.edu).
// ALL RIGHTS RESERVED.
// Changes for DOS and Windows versions by David A. Carley (dac@cs.wisc.edu)
//
// SPIM is distributed under the following conditions:
//
//   You may make copies of SPIM for your own use and modify those copies.
//
//   All copies of SPIM must retain my name and copyright notice.
//
//   You may not sell SPIM or distributed SPIM in conjunction with a
//   commerical product or service without the expressed written consent of
//   James Larus.
//
// THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
// IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
// PURPOSE.

/* $Header: /Software/SPIM/PCSpim/pcspim/MainFrm.cpp 5     12/24/00 1:16p Larus $ */

// MainFrm.cpp : implementation of the CMainFrame class
//

#include "stdafx.h"
#include "PCSpim.h"
#include "PCSpimView.h"
#include "SpimReg.h"

#include "MainFrm.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CMainFrame

IMPLEMENT_DYNCREATE(CMainFrame, CFrameWnd)

BEGIN_MESSAGE_MAP(CMainFrame, CFrameWnd)
	//{{AFX_MSG_MAP(CMainFrame)
	ON_WM_CREATE()
	ON_WM_SIZE()
	//}}AFX_MSG_MAP
	// Global help commands
	ON_COMMAND(ID_HELP_FINDER, CFrameWnd::OnHelpFinder)
	ON_COMMAND(ID_HELP, CFrameWnd::OnHelp)
	ON_COMMAND(ID_CONTEXT_HELP, CFrameWnd::OnContextHelp)
	ON_COMMAND(ID_DEFAULT_HELP, CFrameWnd::OnHelpFinder)
END_MESSAGE_MAP()

static UINT indicators[] =
{
	ID_SEPARATOR,           // status line indicator
	ID_INDICATOR_REGS,
	ID_INDICATOR_BARE,
	ID_INDICATOR_DELAY_BR,
	ID_INDICATOR_DELAY_LD
};

/////////////////////////////////////////////////////////////////////////////
// CMainFrame construction/destruction

CMainFrame::CMainFrame()
{
  // TODO: add member initialization code here

}


CMainFrame::~CMainFrame()
{
}


int CMainFrame::OnCreate(LPCREATESTRUCT lpCreateStruct)
{
  if (CFrameWnd::OnCreate(lpCreateStruct) == -1)
    {
      return -1;
    }

  if (!m_wndToolBar.Create(this) ||
      !m_wndToolBar.LoadToolBar(IDR_MAINFRAME))
    {
      TRACE0("Failed to create toolbar\n");
      return -1;      // fail to create
    }

  if (!m_wndStatusBar.Create(this) ||
      !m_wndStatusBar.SetIndicators(indicators,
				    sizeof(indicators)/sizeof(UINT)))
    {
      TRACE0("Failed to create status bar\n");
      return -1;      // fail to create
    }
  m_wndStatusBar.SetPaneStyle(1, SBPS_STRETCH);

  // TODO: Remove this if you don't want tool tips or a resizeable toolbar
  m_wndToolBar.SetBarStyle(m_wndToolBar.GetBarStyle()
			   | CBRS_TOOLTIPS
			   | CBRS_FLYBY
			   | CBRS_SIZE_DYNAMIC);

  // TODO: Delete these three lines if you don't want the toolbar to be
  // dockable
  m_wndToolBar.EnableDocking(CBRS_ALIGN_ANY);
  EnableDocking(CBRS_ALIGN_ANY);
  DockControlBar(&m_wndToolBar);

  g_pView->PostMessage(WM_COMMAND, MAKEWPARAM(ID_WINDOW_ARRANGEICONS, 0), 0);

  return 0;
}


BOOL CMainFrame::PreCreateWindow(CREATESTRUCT& cs)
{
  // Turn off the filename portion of the window title.
  cs.style &= ~(FWS_ADDTOTITLE | FWS_PREFIXTITLE);

  CPCSpimApp *pApp = (CPCSpimApp *)AfxGetApp();

  g_fSaveWinPos = pApp->GetSetting(SPIM_REG_SAVEWINPOS, TRUE);
  if (g_fSaveWinPos)
    {
      RECT r;
      if (pApp->GetSetting(SPIM_REG_MAINWINPOS, &r))
	{
	  cs.x = r.left;
	  cs.y = r.top;
	  cs.cx = r.right - r.left;
	  cs.cy = r.bottom - r.top;
	}
    }

  return CFrameWnd::PreCreateWindow(cs);
}


/////////////////////////////////////////////////////////////////////////////
// CMainFrame diagnostics

#ifdef _DEBUG
void CMainFrame::AssertValid() const
{
  CFrameWnd::AssertValid();
}


void CMainFrame::Dump(CDumpContext& dc) const
{
  CFrameWnd::Dump(dc);
}

#endif //_DEBUG


/////////////////////////////////////////////////////////////////////////////
// CMainFrame message handlers

LPCTSTR CMainFrame::GetTitleBase()
{
  return m_strTitle;
}

void CMainFrame::UpdateSettingsStatus()
{
  CString strTxt;

  strTxt.Format("PC=0x%08x  EPC=0x%08x  Cause=0x%08x", PC, EPC, Cause);
  m_wndStatusBar.SetPaneText(1, strTxt);
  strTxt.Format("%s", bare_machine ? "BARE" : "");
  m_wndStatusBar.SetPaneText(2, strTxt);
  strTxt.Format("%s", delayed_branches ? "DELAY BR" : "");
  m_wndStatusBar.SetPaneText(3, strTxt);
  strTxt.Format("%s", delayed_loads ? "DELAY LD" : "");
  m_wndStatusBar.SetPaneText(4, strTxt);
  }

void CMainFrame::OnSize(UINT nType, int cx, int cy)
{
  CFrameWnd::OnSize(nType, cx, cy);

  if (nType == SIZE_MINIMIZED)
    {
      g_pView->m_fConsoleMinimized = TRUE;
      g_pView->GetConsole()->ShowWindow(SW_HIDE);
    }
  else if (nType == SIZE_RESTORED)
    {
      g_pView->m_fConsoleMinimized = FALSE;
      g_pView->GetConsole()->ShowWindow(SW_RESTORE);
    }

  RECT r;
  g_pView->GetClientRect(&r);
  g_pView->TileWindows(r.right - r.left, r.bottom - r.top, r.bottom);
}
