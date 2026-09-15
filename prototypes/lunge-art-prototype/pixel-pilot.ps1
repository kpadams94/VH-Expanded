param([switch]$RenderOnly)
$ErrorActionPreference='Stop'
Add-Type -AssemblyName System.Drawing
$root=$PSScriptRoot
$out=Join-Path $root 'native'; New-Item -ItemType Directory -Force $out | Out-Null
# Native-grid silhouette studies: rows are y,xStart,xEnd. No image resampling.
$shapes=@{
 '01'=@(@(2,10,13),@(3,7,11),@(4,5,8),@(5,4,6),@(6,3,5),@(7,3,4),@(8,3,4),@(9,4,5),@(10,4,5),@(11,5,6),@(10,2,2),@(11,1,2),@(12,1,2),@(13,2,4),@(14,4,8),@(13,8,10),@(12,10,11),@(11,9,10),@(11,3,4),@(12,4,7),@(11,7,8))
 '02'=@(@(2,11,14),@(3,8,12),@(4,6,9),@(5,4,7),@(6,3,5),@(7,2,4),@(8,2,4),@(9,3,4),@(10,4,5),@(11,5,6),@(11,1,2),@(12,1,2),@(13,2,4),@(14,4,8),@(13,8,10),@(12,10,11),@(11,9,10),@(11,3,4),@(12,4,7),@(11,7,8))
 '03'=@(@(1,10,12),@(2,7,11),@(3,5,8),@(4,4,6),@(5,3,5),@(6,3,4),@(7,3,4),@(8,4,5),@(9,4,5),@(10,5,6),@(11,6,6),@(11,2,3),@(12,1,2),@(13,2,3),@(14,4,8),@(13,9,10),@(12,10,11),@(11,8,9),@(12,4,8))
}
if(-not $RenderOnly){
 foreach($id in @('01','02','03')){
  $bmp=[Drawing.Bitmap]::new(16,16,[Drawing.Imaging.PixelFormat]::Format32bppArgb)
  foreach($run in $shapes[$id]){for($x=$run[1];$x -le $run[2];$x++){$bmp.SetPixel($x,$run[0],[Drawing.ColorTranslator]::FromHtml('#FFEBA3'))}}
  $bmp.Save((Join-Path $out "$id-silhouette.png"),[Drawing.Imaging.ImageFormat]::Png);$bmp.Dispose()
 }
}
$board=[Drawing.Bitmap]::new(850,440);$g=[Drawing.Graphics]::FromImage($board)
$g.Clear([Drawing.ColorTranslator]::FromHtml('#191C22'))
$g.InterpolationMode=[Drawing.Drawing2D.InterpolationMode]::NearestNeighbor
$g.PixelOffsetMode=[Drawing.Drawing2D.PixelOffsetMode]::Half
$font=[Drawing.Font]::new('Segoe UI',12);$small=[Drawing.Font]::new('Segoe UI',9)
$brush=[Drawing.Brushes]::White
$g.DrawString('LUNGE / NATIVE-GRID SILHOUETTE STUDIES', $font,$brush,20,12)
$g.DrawString('One source pixel per cell. Small views are 1x and 3x; inspection views are 12x.', $small,$brush,20,40)
$i=0
foreach($id in @('01','02','03')){
 $x=25+$i*280;$im=[Drawing.Bitmap]::new((Join-Path $out "$id-silhouette.png"))
 $g.DrawString("Candidate $id",$font,$brush,$x,75)
 $g.DrawImage($im,[Drawing.Rectangle]::new($x,112,16,16),0,0,16,16,[Drawing.GraphicsUnit]::Pixel)
 $g.DrawImage($im,[Drawing.Rectangle]::new($x+50,100,48,48),0,0,16,16,[Drawing.GraphicsUnit]::Pixel)
 $g.DrawImage($im,[Drawing.Rectangle]::new($x,175,192,192),0,0,16,16,[Drawing.GraphicsUnit]::Pixel)
 $im.Dispose();$i++
}
$g.Dispose();$board.Save((Join-Path $root 'silhouette-review.png'),[Drawing.Imaging.ImageFormat]::Png);$board.Dispose()
